package com.zippy.api.rest;

import com.zippy.api.document.Vehicle;
import com.zippy.api.dto.VehicleDTO;
import com.zippy.api.service.VehicleService;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleREST {
    private final VehicleService vehicleService;

    public VehicleREST(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<Vehicle>> allVehicles() {
        return ResponseEntity.ok(vehicleService.allVehicles());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> addVehicle(VehicleDTO dto) {
        Vehicle vehicle =
                new Vehicle(
                        dto.getType(),
                        dto.getModel(),
                        dto.getGpsSerial(),
                        dto.getSerial(),
                        dto.isElectric()
                );
        vehicle.setBattery(
                dto.isElectric()
                        ? dto.getBattery()
                        : 0
        );
        return ResponseEntity.ok(vehicleService.addVehicle(vehicle));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable ObjectId id) {
        vehicleService.deleteVehicleById(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/battery/{id}")
    public ResponseEntity<?> updateBattery(@PathVariable ObjectId id, @RequestBody int battery) {
        Vehicle vehicle = vehicleService.getVehicleById(id);
        vehicle.setBattery(battery);
        return ResponseEntity.ok(vehicleService.addVehicle(vehicle));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getVehicle(@PathVariable ObjectId id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }
}
