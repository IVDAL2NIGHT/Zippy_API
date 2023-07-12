package com.zippy.api.service;

import com.zippy.api.constants.VehicleStatus;
import com.zippy.api.document.Station;
import com.zippy.api.exception.StationNotFoundException;
import com.zippy.api.models.VehicleStatusId;
import com.zippy.api.repository.StationRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class StationService {
    private final StationRepository stationRepository;
    private final VehicleService vehicleService;

    @Autowired
    public StationService(StationRepository stationRepository, VehicleService vehicleService) {
        this.stationRepository = stationRepository;
        this.vehicleService = vehicleService;

    }

    @PreAuthorize("hasRole('ADMIN')")
    public Station addStation(Station station) {
        return stationRepository.save(station);
    }

    public void deleteStationById(ObjectId id) {
        stationRepository.deleteById(id);
    }

    public Station getStationById(ObjectId id) throws StationNotFoundException {
        return stationRepository.findById(id).orElseThrow(() -> new StationNotFoundException("El id de la estación no existe"));
    }

    public Station getStationByName(String name) {
        return stationRepository.findByName(name);
    }

    public Station addVehicleToStation(ObjectId stationId, ObjectId vehicleId) throws StationNotFoundException {
        Station station = stationRepository.findById(stationId).orElseThrow(() -> new StationNotFoundException("El id de la estación no existe"));
        station.getVehicleStatusIds().add(new VehicleStatusId(vehicleId, vehicleService.getVehicleById(vehicleId).getStatus()));
        return stationRepository.save(station);
    }

    public Station removeVehicleFromStation(ObjectId stationId, ObjectId vehicle) throws StationNotFoundException {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new StationNotFoundException("El id de la estación no existe"));
        station.getVehicleStatusIds().remove(new VehicleStatusId(vehicle, vehicleService.getVehicleById(vehicle).getStatus()));
        return stationRepository.save(station);
    }

    public List<ObjectId> getAvailableVehiclesByStationId(ObjectId stationId) throws StationNotFoundException {
        return Objects.requireNonNull(stationRepository.findById(stationId)
                        .orElseThrow(() -> new StationNotFoundException("El id de la estación no existe")))
                .getVehicleStatusIds()
                .stream()
                .filter(vehicleStatusId -> vehicleStatusId.getStatus().equals(VehicleStatus.AVAILABLE))
                .map(VehicleStatusId::getVehicleId)
                .toList();
    }

    public List<Station> allStations() {
        return stationRepository.findAll();
    }

    public List<ObjectId> getVehiclesByStationId(ObjectId id) {
        return Objects.requireNonNull(stationRepository.findById(id).orElse(null))
                .getVehicleStatusIds()
                .stream()
                .map(VehicleStatusId::getVehicleId)
                .toList();
    }
}
