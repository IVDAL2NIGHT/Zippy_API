package com.zippy.api.rest;

import com.zippy.api.document.Station;
import com.zippy.api.dto.StationDTO;
import com.zippy.api.models.GeoJsonStation;
import com.zippy.api.models.GeoJsonStationCollection;
import com.zippy.api.models.geoJsonResponse.GeoJsonResponseWraper;
import com.zippy.api.service.StationService;
import com.zippy.api.service.VehicleService;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
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

    @PreAuthorize("hasAuthority('ADMIN, EMPLOYEE')")
    @PostMapping("/{id}/add-vehicle/{VehicleSerial}")
    public ResponseEntity<?> addVehicleToStationBySerial(@PathVariable ObjectId id, @PathVariable String VehicleSerial) {
        return ResponseEntity.ok(stationService.addVehicle(stationService.getById(id), vehicleService.getBySerial(VehicleSerial)));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}/remove-vehicle/{vehicleId}")
    public ResponseEntity<?> removeVehicleFromStation(@PathVariable ObjectId id, @PathVariable ObjectId vehicleId) {
        return ResponseEntity.ok(
                stationService.removeVehicle(stationService.getById(id), vehicleService.getById(vehicleId))
        );
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> addStation(@RequestBody StationDTO dto) {
        return ResponseEntity.ok(stationService.add(
                new Station()
                        .setId(new ObjectId())
                        .setName(dto.name())
                        .setCapacity(dto.capacity())
                        .setVehicleStatusIds(dto.vehicleStatusIdList())
                        .setStatus(dto.status())
                        .setLocation(new GeoJsonPoint(
                                dto.location().getLongitude(), dto.location().getLatitude()
                        ))
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStationById(@PathVariable ObjectId id) {
        return ResponseEntity.ok(stationService.getById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<GeoJsonStationCollection> allStations() {
        return ResponseEntity.ok(
                new GeoJsonStationCollection(
                        stationService.all()
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

    @GetMapping("/route/{start}/{end}")
    public ResponseEntity<?> getRoute(@PathVariable ObjectId start, @PathVariable ObjectId end) {
        try {
            GeoJsonResponseWraper route = stationService.calculateRoute(
                    stationService.getById(start), stationService.getById(end)
            );
            if (route.statusCode() == 200)
                return ResponseEntity.ok(route.featureCollection());
            else
                return ResponseEntity.internalServerError().body(route.statusMessage());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
