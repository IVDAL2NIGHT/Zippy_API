package com.zippy.api.rest;

import com.zippy.api.dto.StationDTO;
import com.zippy.api.models.GeoJsonStation;
import com.zippy.api.models.GeoJsonStationCollection;
import com.zippy.api.service.StationService;
import com.zippy.api.service.VehicleService;
import org.bson.types.ObjectId;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stations")
public class StationREST {
    private final StationService stationService;
    private final VehicleService vehicleService;

    public StationREST(StationService stationService, VehicleService vehicleService) {
        this.stationService = stationService;
        this.vehicleService = vehicleService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}/add-vehicle/{VehicleSerial}")
    public ResponseEntity<?> addVehicleToStationBySerial(@PathVariable ObjectId id, @PathVariable String VehicleSerial) {
        return ResponseEntity.ok(stationService.addVehicleToStation(stationService.getStationById(id), vehicleService.getVehicleBySerial(VehicleSerial)));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}/remove-vehicle/{vehicleId}")
    public ResponseEntity<?> removeVehicleFromStation(@PathVariable ObjectId id, @PathVariable ObjectId vehicleId) {
        stationService.removeVehicleFromStation(stationService.getStationById(id), vehicleId);
        return ResponseEntity.ok().build();
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> addStation(@RequestBody StationDTO dto) {
        return ResponseEntity.ok(stationService.saveStation(
                stationService.createStation(dto)
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStationById(@PathVariable ObjectId id) {
        return ResponseEntity.ok(stationService.getStationById(id));
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        stationService.test();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<GeoJsonStationCollection> allStations() {
        return ResponseEntity.ok(
                new GeoJsonStationCollection(
                        stationService.getAllStations()
                                .stream()
                                .map(GeoJsonStation::new)
                                .toArray(GeoJsonStation[]::new)
                )
        );
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}/vehicles")
    public ResponseEntity<?> getVehiclesByStationId(@PathVariable ObjectId id) {
        return ResponseEntity.ok(stationService.getVehiclesByStationId(id));
    }

    @GetMapping("/{id}/available-vehicles")
    public ResponseEntity<?> getAvailableVehiclesByStationId(@PathVariable ObjectId id) {
        return ResponseEntity.ok(stationService.getAvailableVehiclesByStationId(id));
    }

}
