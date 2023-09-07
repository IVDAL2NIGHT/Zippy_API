package com.zippy.api.rest;

import com.zippy.api.constants.VehicleStatus;
import com.zippy.api.constants.VehicleType;
import com.zippy.api.document.Vehicle;
import com.zippy.api.dto.VehicleDTO;
import com.zippy.api.exception.VehicleNotFoundException;
import com.zippy.api.service.VehicleService;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

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
    public ResponseEntity<?> addVehicle(@Valid @RequestBody VehicleDTO dto) {
        System.out.println("dto: " + dto.toString());
        Vehicle vehicle = vehicleService.add(
                Vehicle.builder()
                        .type((dto.getType()))
                        .model(dto.getModel())
                        .gpsSerial(dto.getGpsSerial())
                        .serial(dto.getSerial())
                        .isElectric(dto.isElectric())
                        .status(VehicleStatus.AVAILABLE)
                        .startUpDate(LocalDateTime.now())
                        .kilometers(0)
                        .maintenances(new ArrayList<>())
                        .id(new ObjectId())
                        .battery(dto.isElectric() ? dto.getBattery() : 0)
                        .build()
        );
        return ResponseEntity.created(URI.create("/api/vehicle/" + vehicle.getId())).body(vehicle);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable ObjectId id) {
        vehicleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/update/battery/{id}")
    public ResponseEntity<?> updateBattery(@PathVariable ObjectId id, @Valid @RequestBody int battery) {
        return ResponseEntity.ok(vehicleService.save(
                vehicleService.getById(id).setBattery(battery)
        ));
    }



    @GetMapping("/{id}")
    public ResponseEntity<?> getVehicle(@PathVariable ObjectId id) {
        try {
            Vehicle vehicle = vehicleService.getById(id);
            return ResponseEntity.ok(vehicle);
        } catch (VehicleNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
}
