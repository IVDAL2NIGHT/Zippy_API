package com.zippy.api.rest;

import com.zippy.api.constants.TripStatus;
import com.zippy.api.constants.VehicleStatus;
import com.zippy.api.document.Credential;
import com.zippy.api.document.Trip;
import com.zippy.api.dto.EndTripDTO;
import com.zippy.api.dto.TripDTO;
import com.zippy.api.service.StationService;
import com.zippy.api.service.TripService;
import com.zippy.api.service.VehicleService;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/trip")
public class TripREST {

    private final TripService tripService;
    private final VehicleService vehicleService;
    private final StationService stationService;

    public TripREST(TripService tripService, VehicleService vehicleService, StationService stationService) {
        this.tripService = tripService;
        this.vehicleService = vehicleService;
        this.stationService = stationService;
    }

    @GetMapping("/getTrip/{tripId}")
    public ResponseEntity<Trip> getTripById(@PathVariable ObjectId tripId) {
        return ResponseEntity.ok(tripService.getTripById(tripId));
    }

    @GetMapping("/getAllUserTrips/{userId}")
    public ResponseEntity<List<Trip>> getAllUserTrips(@PathVariable ObjectId userId) {
        return ResponseEntity.ok(tripService.getAllUserTrips(userId));
    }

    @PostMapping("/startNewTrip")
    public ResponseEntity<?> startNewTrip (@AuthenticationPrincipal Credential credential , @NotNull @Valid @RequestBody TripDTO dto) {
        try{
            stationService.getById(dto.getStartStationId());
            stationService.getById(dto.getEndStationId());

            if (tripService.getTripByUserIdAndStatus(credential.getUserId(), TripStatus.ACTIVE).isPresent()
                    || tripService.getTripByUserIdAndStatus(credential.getUserId(), TripStatus.RESERVED).isPresent()) {

                return ResponseEntity.badRequest().body("The user with ID " + credential.getUserId() + "have an active or reserved trip. Users only can take a trip at time");
            } else if (vehicleService.getById(dto.getVehicleId()).getStatus() != VehicleStatus.AVAILABLE) {
                return ResponseEntity.badRequest().body("The status of the vehicle is not longer available");
            } else
                return ResponseEntity.ok(tripService.startTrip(credential.getUserId(), dto.getVehicleId(), dto.getStartStationId(), dto.getEndStationId(), dto.getDistance(), dto.getDuration()));
        }catch (Error e){
            return ResponseEntity.badRequest().body(e.getCause());
        }
    }

    @PostMapping("/reserveTrip")
    public ResponseEntity<?> reserveTrip (@AuthenticationPrincipal Credential credential , @NotNull @Valid @RequestBody TripDTO dto) {

        if (tripService.getTripByUserIdAndStatus(credential.getUserId(), TripStatus.ACTIVE).isPresent()
                || tripService.getTripByUserIdAndStatus(credential.getUserId(), TripStatus.RESERVED).isPresent()){

            return ResponseEntity.badRequest().body("The user with ID " + credential.getUserId() + "have an active or reserved trip. Users only can take a trip at time");

        } else if (vehicleService.getById(dto.getVehicleId()).getStatus() != VehicleStatus.AVAILABLE ) {
            return ResponseEntity.badRequest().body("The status of the vehicle is not longer available");
        }else
            return ResponseEntity.ok(tripService.reserveTrip(credential.getUserId(), dto.getVehicleId(), dto.getStartStationId(), dto.getEndStationId(), dto.getDistance()));
    }

    @PutMapping("/startReserveTrip/{tripDuration}")
    public ResponseEntity<?> startReserveTrip (@AuthenticationPrincipal Credential credential , @Valid @PathVariable int tripDuration) {

        Optional<Trip> reservedTrip = tripService.getTripByUserIdAndStatus(credential.getUserId(), TripStatus.RESERVED);
        if (reservedTrip.isEmpty()) {
            return ResponseEntity.badRequest().body("The user with ID " + credential.getUserId() + " doesn't have a reserved trip");
        } else {
            return ResponseEntity.ok(tripService.startReserveTrip(reservedTrip.get(), tripDuration));
        }
    }

    @PutMapping("/cancelReserveTrip")
    public ResponseEntity<?> cancelReserveTrip (@AuthenticationPrincipal Credential credential){
        Optional<Trip> reservedTrip = tripService.getTripByUserIdAndStatus(credential.getUserId(), TripStatus.RESERVED);
        if (reservedTrip.isEmpty()){
            return ResponseEntity.badRequest().body("The user with ID " + credential.getUserId() + " doesn't have a reserved trip");
        } else {
            tripService.cancelReserveTrip(reservedTrip.get());
            return ResponseEntity.ok().build();
        }
    }

    @PutMapping("/endActualTrip")
    public ResponseEntity<?> endActualTrip (@AuthenticationPrincipal Credential credential, @Validated @RequestBody Optional<EndTripDTO> dto){
        if(tripService.getTripByUserIdAndStatus(credential.getUserId(), TripStatus.ACTIVE).isEmpty()){
            return ResponseEntity.badRequest().body("The user have not an active trip to end");
        }else {

            String userComment = dto.map(EndTripDTO::getUserComment).orElse(null);
            Integer userRating = dto.map(EndTripDTO::getUserRating).orElse(null);

            tripService.endActualTrip(credential.getUserId(), userRating, userComment);
            return ResponseEntity.ok("Trip have been finalized successfully");
        }
    }

}
