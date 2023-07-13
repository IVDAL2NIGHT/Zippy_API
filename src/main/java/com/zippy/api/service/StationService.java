package com.zippy.api.service;

import com.zippy.api.constants.VehicleStatus;
import com.zippy.api.document.Station;
import com.zippy.api.document.Vehicle;
import com.zippy.api.dto.StationDTO;
import com.zippy.api.exception.DuplicatedVehicleException;
import com.zippy.api.exception.StationNotFoundException;
import com.zippy.api.exception.VehicleNotFoundException;
import com.zippy.api.models.VehicleStatusId;
import com.zippy.api.repository.StationRepository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }

    public Station createStation(StationDTO dto) {
        return new Station(
                new ObjectId(),
                dto.getName(),
                new GeoJsonPoint(dto.getLocation().getLatitude(), dto.getLocation().getLongitude()),
                dto.getCapacity(),
                dto.getVehicleStatusIdList(),
                dto.getStatus()
        );
    }
    public Station saveStation(Station station) {
        return stationRepository.save(station);
    }

    public void deleteStationById(ObjectId id) {
        stationRepository.deleteById(id);
    }

    public Station getStationById(ObjectId id) throws StationNotFoundException {
        return stationRepository.findById(id)
                .orElseThrow(() -> new StationNotFoundException("El id de la estación no existe"));
    }

    public Station getStationByName(String name) throws StationNotFoundException {
        return stationRepository.findByName(name)
                .orElseThrow(() -> new StationNotFoundException("El nombre de la estación no existe"));
    }

    public Station addVehicleToStation(Station station, Vehicle vehicle) throws DuplicatedVehicleException {
        VehicleStatusId vehicleStatusId = new VehicleStatusId(vehicle.getId(), vehicle.getStatus());
        if (station.getVehicleStatusIds().contains(vehicleStatusId)) {
            throw new DuplicatedVehicleException("El vehículo ya está asociado a la estación");
        }
        station.getVehicleStatusIds().add(vehicleStatusId);
        return stationRepository.save(station);
    }

    public void removeVehicleFromStation(Station station, ObjectId vehicle) throws VehicleNotFoundException {
        station.getVehicleStatusIds()
                .remove(
                        station.getVehicleStatusIds()
                                .stream()
                                .filter(vehicleStatusId -> vehicleStatusId.get_id().equals(vehicle))
                                .findFirst()
                                .orElseThrow(() -> new VehicleNotFoundException("El vehículo no está asociado a la estación"))
                );
        stationRepository.save(station);
    }

    public void test(){
        String apikey = "${com.zippy.api.openroutekey}";
        String bCoordinates ="8.681495,49.41461";
        String eCoordinates ="8.687872,49.420318";
        String url = "https://api.openrouteservice.org/v2/directions/driving-car?api_key=" + apikey + "&start=" + bCoordinates + "&end=" + eCoordinates;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        response.thenAccept(res -> {
            int statusCode = res.statusCode();
            HttpHeaders headers = res.headers();
            String responseBody = res.body();

            System.out.println("Status Code: " + statusCode);
            System.out.println("Headers: " + headers);
            System.out.println("Response Body: " + responseBody);
        }).join();
    }

    public List<ObjectId> getAvailableVehiclesByStationId(ObjectId id) {
        return getStationById(id)
                .getVehicleStatusIds()
                .stream()
                .filter(entry -> entry.getStatus().equals(VehicleStatus.AVAILABLE))
                .map(VehicleStatusId::get_id)
                .toList();
    }

    public List<ObjectId> getVehiclesByStationId(ObjectId id) {
        return getStationById(id)
                .getVehicleStatusIds()
                .stream()
                .map(VehicleStatusId::get_id)
                .toList();
    }
}
