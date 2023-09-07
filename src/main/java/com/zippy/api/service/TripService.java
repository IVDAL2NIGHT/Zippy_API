package com.zippy.api.service;

import com.zippy.api.constants.TripStatus;
import com.zippy.api.constants.VehicleStatus;
import com.zippy.api.document.Trip;
import com.zippy.api.exception.TripNotFoundException;
import com.zippy.api.repository.TripRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class TripService {

    private final TripRepository tripRepository;
    private final VehicleService vehicleService;

    public TripService(TripRepository tripRepository, VehicleService vehicleService) {
        this.tripRepository = tripRepository;
        this.vehicleService = vehicleService;
    }
    public Trip getTripById(ObjectId tripId) throws TripNotFoundException {
        return tripRepository.findById(tripId).orElseThrow(() -> new TripNotFoundException("A trip with ID " + tripId.toString() + "has not been found."));
    }

    public List<Trip> getAllUserTrips(ObjectId userId){
        return tripRepository.findByUserId(userId).orElseThrow(() -> new TripNotFoundException("The user with ID " + userId.toString() + "has not made any trips"));
    }


    public Optional<Trip> getTripByUserIdAndStatus(ObjectId userId, TripStatus tripStatus) {
            return  tripRepository.findByUserIdAndStatus(userId, tripStatus);
    }

    //Instant creation of a travel
    public Trip startTrip (ObjectId userid, ObjectId vehicleId, ObjectId startStationId, ObjectId endStationId, BigDecimal distance, int duration) {
        vehicleService.updateStatus(vehicleId, VehicleStatus.UNAVAILABLE);
        return
                    tripRepository.insert(
                            new Trip()
                                    .setUserId(userid)
                                    .setVehicleId(vehicleId)
                                    .setStartStationId(startStationId)
                                    .setEndStationId(endStationId)
                                    .setStartDate(LocalDateTime.now())
                                    .setStatus(TripStatus.ACTIVE)
                                    .setCost(calculateInitialCost(distance, vehicleId))
                                    .setDeadLine(calculateDeadline(duration))
                    );
    }

    public Trip reserveTrip(ObjectId userid, ObjectId vehicleId, ObjectId startStationId, ObjectId endStationId, BigDecimal distance){

        vehicleService.updateStatus(vehicleId, VehicleStatus.RESERVED);

        return
                tripRepository.insert(
                        new Trip()
                                .setUserId(userid)
                                .setVehicleId(vehicleId)
                                .setStartStationId(startStationId)
                                .setEndStationId(endStationId)
                                .setStatus(TripStatus.RESERVED)
                                .setCost(calculateInitialCost(distance, vehicleId))
                                .setDeadLine(LocalDateTime.now().plusMinutes(10))
                                .setReservedDate(LocalDateTime.now())
                );
    };

    public Trip startReserveTrip(Trip trip, int durationOfTrip){
        //Recalculate the cost of the reserved trip, adding a penalty if the user misses the deadline
        trip.setCost(calculateFinalCost(trip));
        trip.setStartDate(LocalDateTime.now());
        trip.setDeadLine(calculateDeadline(durationOfTrip));
        trip.setStatus(TripStatus.ACTIVE);
        return tripRepository.save(trip);
    }

    public void endActualTrip (ObjectId userId, Integer userRating, String userComment) {
        Trip trip = getTripByUserIdAndStatus(userId, TripStatus.ACTIVE).orElseThrow(()->new TripNotFoundException("The user have not an active trip. Validation in tripREST failed"));
        BigDecimal cost = calculateFinalCost(trip);

        trip.setStatus(TripStatus.COMPLETED);
        trip.setCost(cost);
        trip.setEndDate(LocalDateTime.now());
        if (userComment != null) { trip.setUserComment(userComment);}
        if(userRating != null) {trip.setUserRating(userRating);}


        tripRepository.save(trip);

        vehicleService.updateStatus(trip.getVehicleId(), VehicleStatus.AVAILABLE);

    }

    public void cancelReserveTrip (Trip trip){

        trip.setStatus(TripStatus.CANCELLED);
        vehicleService.updateStatus(trip.getVehicleId(), VehicleStatus.AVAILABLE);
        LocalDateTime deadLine = trip.getDeadLine();
        if(deadLine.isBefore(LocalDateTime.now())){
            BigDecimal priceExtraMinute = BigDecimal.valueOf(50);
            BigDecimal minutesDifference = BigDecimal.valueOf(Duration.between(deadLine, LocalDateTime.now()).toMinutes());
            BigDecimal cost = minutesDifference.multiply(priceExtraMinute);
        }
    }

    public BigDecimal calculateInitialCost (BigDecimal distance, ObjectId vehicleId) {

        //Cheaper price for Not electric vehicles
        boolean isElectric = vehicleService.getById(vehicleId).isElectric();
        BigDecimal baseCost = isElectric ? BigDecimal.valueOf(1000) : BigDecimal.valueOf(500);

        //It is calculated proportionally to the base price, taking the distance in kilometers divided by 100 and multiplied by a price-increasing factor.
        int multiplyCostFactor = 2;
        return baseCost.add(baseCost.multiply(distance.divide(BigDecimal.valueOf(100 / multiplyCostFactor), 2, RoundingMode.HALF_UP)));
    }

    public BigDecimal calculateFinalCost (Trip trip){

        BigDecimal initialCost = trip.getCost();
        LocalDateTime deadline = trip.getDeadLine();

        //Price in colombian pesos for extra minute
        BigDecimal priceExtraMinute =  BigDecimal.valueOf(50);

        if(deadline.isBefore(LocalDateTime.now())) {
            BigDecimal minutesDifference = BigDecimal.valueOf(Duration.between(deadline, LocalDateTime.now()).toMinutes());
            initialCost = initialCost.add(minutesDifference.multiply(priceExtraMinute));
        }

        return initialCost;
    }

    public LocalDateTime calculateDeadline(int duration){

        //Extra minutes without an additional cost that user will have in case of traffic or personal circumstances
        int marginMinutes = 10;

        return LocalDateTime.now().plusMinutes(duration+marginMinutes);

    }


}
