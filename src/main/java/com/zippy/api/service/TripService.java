package com.zippy.api.service;

import com.zippy.api.constants.TripStatus;
import com.zippy.api.document.Station;
import com.zippy.api.document.Trip;
import com.zippy.api.models.Location;
import com.zippy.api.repository.StationRepository;
import com.zippy.api.repository.TripRepository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class TripService {

    private final TripRepository tripRepository;


    private final StationRepository stationRepository;

    public TripService(TripRepository tripRepository, StationRepository stationRepository) {
        this.tripRepository = tripRepository;
        this.stationRepository = stationRepository;
    }

    //Creación inicial del viaje
    public Trip createTrip(ObjectId userid, ObjectId vehicleId, ObjectId startStationId, ObjectId endStationId) {

        return (tripRepository.insert(new Trip(userid, vehicleId, startStationId, endStationId, calculateCost(startStationId, endStationId), TripStatus.ACTIVE, calculateDeadline(startStationId, endStationId))));
    }

    private BigDecimal calculateCost(ObjectId startStationId, ObjectId endStationId) {

        BigDecimal cost = BigDecimal.valueOf(1000);
        BigDecimal distance = BigDecimal.valueOf(1000);

        //obtenemos las coordenadas de las estaciones
        Optional<Station> startStation = stationRepository.findById(startStationId);
        Optional<Station> endStation = stationRepository.findById(endStationId);

        if (startStation.isPresent() && endStation.isPresent()) {
            GeoJsonPoint startLocation = startStation.get().getLocation();
            GeoJsonPoint endLocation = endStation.get().getLocation();
        }

        return cost;
    }


    private LocalDateTime calculateDeadline(ObjectId startStationId, ObjectId endStationId) {
        LocalDateTime deadline = LocalDateTime.now().plusMinutes(30);
        return deadline;
    }
}
