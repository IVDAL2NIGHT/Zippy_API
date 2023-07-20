package com.zippy.api.rest;

import com.zippy.api.document.Vehicle;
import com.zippy.api.dto.VehicleDTO;
import com.zippy.api.service.VehicleService;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleREST {
    private final VehicleService vehicleService;

    public VehicleREST(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<Vehicle>> allVehicles() {
        return ResponseEntity.ok(vehicleService.all());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> addVehicle(VehicleDTO dto) {
        return ResponseEntity.ok(vehicleService.add(
                        Vehicle.builder()
                                .type(dto.type())
                                .model(dto.model())
                                .gpsSerial(dto.gpsSerial())
                                .serial(dto.serial())
                                .isElectric(dto.isElectric())
                                .status(dto.status())
                                .startUpDate(LocalDateTime.now())
                                .kilometers(0)
                                .maintenances(null)
                                .id(new ObjectId())
                                .battery(dto.isElectric() ? dto.battery() : 0)
                                .build()
                )
        );
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable ObjectId id) {
        vehicleService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/update/battery/{id}")
    public ResponseEntity<?> updateBattery(@PathVariable ObjectId id, @RequestBody int battery) {
        return ResponseEntity.ok(vehicleService.save(
                vehicleService.getById(id).setBattery(battery)
        ));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getVehicle(@PathVariable ObjectId id) {
        return ResponseEntity.ok(vehicleService.getById(id));
    }
}
