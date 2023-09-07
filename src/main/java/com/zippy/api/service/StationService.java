package com.zippy.api.service;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zippy.api.constants.VehicleStatus;
import com.zippy.api.document.Station;
import com.zippy.api.document.Vehicle;
import com.zippy.api.exception.StationNotFoundException;
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

@Service
public class StationService {
    private final StationRepository stationRepository;
    private final VehicleService vehicleService;

    public StationService(StationRepository stationRepository, VehicleService vehicleService) {
        this.stationRepository = stationRepository;
        this.vehicleService = vehicleService;
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

    public List<Vehicle> getAvailableVehiclesByStationId(ObjectId id) {
        return getById(id)
                .getVehicleStatusIds()
                .stream()
                .filter(entry -> entry.getStatus().equals(VehicleStatus.AVAILABLE))
                .map(VehicleStatusId::getId)
                .map(vehicleService::getById)
                .toList();
    }

    public List<ObjectId> getVehiclesByStationId(ObjectId id) {
        return getById(id)
                .getVehicleStatusIds()
                .stream()
                .map(VehicleStatusId::getId)
                .toList();
    }

    public GeoJsonResponseWraper calculateRoute(Station startStation, Station endStation) {
        try {
            GeoRequest requestValue = GeoRequest.builder()
                    .coordinates(new Double[][]{
                            startStation.getCoordinates(),
                            endStation.getCoordinates()
                    })
                    .elevation(false)
                    .language("es")
                    .preference("recommended")
                    .units("m")
                    .geometry(true)
                    .build();

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
            return GeoJsonResponseWraper.builder()
                    .statusCode(response.statusCode())
                    .statusMessage(response.body())
                    .featureCollection(
                            Optional.ofNullable(objectMapper.readValue(response.body(), FeatureCollection.class))
                            .orElseThrow(() -> new Exception("Error al parsear la respuesta de la API de rutas")))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return GeoJsonResponseWraper.builder()
                    .statusCode(500)
                    .statusMessage("Error al calcular la ruta")
                    .build();
        }
    }
}
