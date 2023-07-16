package com.zippy.api.service;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zippy.api.constants.VehicleStatus;
import com.zippy.api.document.Station;
import com.zippy.api.document.Vehicle;
import com.zippy.api.exception.DuplicatedVehicleException;
import com.zippy.api.exception.StationNotFoundException;
import com.zippy.api.exception.VehicleNotFoundException;
import com.zippy.api.models.VehicleStatusId;
import com.zippy.api.models.geoJsonRequest.GeoRequest;
import com.zippy.api.models.geoJsonResponse.FeatureCollection;
import com.zippy.api.models.geoJsonResponse.GeoJsonResponseWraper;
import com.zippy.api.repository.StationRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public List<Station> all() {
        return stationRepository.findAll();
    }

    public Station add(Station station) {
        return stationRepository.insert(station);
    }

    public Station save(Station station) {
        return stationRepository.save(station);
    }

    public void delete(ObjectId id) {
        stationRepository.deleteById(id);
    }

    public Station getById(ObjectId id) throws StationNotFoundException {
        return stationRepository.findById(id)
                .orElseThrow(() -> new StationNotFoundException("El id de la estación no existe"));
    }

    public Station getByName(String name) throws StationNotFoundException {
        return stationRepository.findByName(name)
                .orElseThrow(() -> new StationNotFoundException("El nombre de la estación no existe"));
    }

    public Station addVehicle(Station station, Vehicle vehicle) throws DuplicatedVehicleException {
        if (station.getVehicleStatusIds().stream().anyMatch(vehicleStatusId -> vehicleStatusId.get_id().equals(vehicle.getId()))) {
            throw new DuplicatedVehicleException("El vehículo ya se encuentra en la estación");
        }
        if (vehicle.getStatus() == VehicleStatus.AVAILABLE) {
            throw new Error("El vehículo no puede agregarse a la estación si está asignado a otra estación");
        }
        vehicle.setStatus(VehicleStatus.AVAILABLE);
        return save(
                station.setVehicleStatusIds(
                        Stream.concat(
                                station.getVehicleStatusIds().stream(),
                                Stream.of(
                                        new VehicleStatusId()
                                                .set_id(vehicle.getId())
                                                .setStatus(vehicle.getStatus())
                                )
                        ).toList()
                )
        );
    }

    public Station removeVehicle(Station station, Vehicle vehicle) throws VehicleNotFoundException {
        if (station.getVehicleStatusIds().stream().noneMatch(vehicleStatusId -> vehicleStatusId.get_id().equals(vehicle.getId()))) {
            throw new VehicleNotFoundException("El vehículo no se encuentra en la estación");
        }
        return save(
                station.setVehicleStatusIds(
                        station.getVehicleStatusIds()
                                .stream()
                                .filter(vehicleStatusId -> !vehicleStatusId.get_id().equals(vehicle.getId()))
                                .toList()
                )
        );
    }

    public List<ObjectId> getAvailableVehiclesByStationId(ObjectId id) {
        return getById(id)
                .getVehicleStatusIds()
                .stream()
                .filter(entry -> entry.getStatus().equals(VehicleStatus.AVAILABLE))
                .map(VehicleStatusId::get_id)
                .toList();
    }

    public List<ObjectId> getVehiclesByStationId(ObjectId id) {
        return getById(id)
                .getVehicleStatusIds()
                .stream()
                .map(VehicleStatusId::get_id)
                .toList();
    }

    public GeoJsonResponseWraper calculateRoute(Station startStation, Station endStation) {
        try {
            GeoRequest requestValue = new GeoRequest()
                    .coordinates(new Double[][]{
                            startStation.getLocation().getCoordinates().toArray(Double[]::new),
                            endStation.getLocation().getCoordinates().toArray(Double[]::new)
                    })
                    .elevation(false)
                    .language("es")
                    .preference("recommended")
                    .units("m")
                    .geometry(true);

            String payload = requestValue.toJson();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.openrouteservice.org/v2/directions/driving-car/geojson"))
                    .header("Authorization", "${com.zippy.api.openroutekey")
                    .header("Accept", "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8")
                    .header("Content-Type", "application/json; charset=utf-8")
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return new GeoJsonResponseWraper()
                    .statusCode(response.statusCode())
                    .statusMessage(response.body())
                    .featureCollection(Optional.ofNullable(objectMapper.readValue(response.body(), FeatureCollection.class))
                            .orElseThrow(() -> new Exception("Error al parsear la respuesta de la API de rutas")
                            ));
        } catch (Exception e) {
            e.printStackTrace();
            return new GeoJsonResponseWraper()
                    .statusCode(500)
                    .statusMessage("Error al calcular la ruta");
        }
    }
}
