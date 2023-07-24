package com.zippy.api.rest;

import com.zippy.api.document.Station;
import com.zippy.api.dto.StationDTO;
import com.zippy.api.models.GeoJsonStation;
import com.zippy.api.models.GeoJsonStationCollection;
import com.zippy.api.models.geoJsonResponse.GeoJsonResponseWraper;
import com.zippy.api.service.StationService;
import com.zippy.api.service.VehicleService;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;


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
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStation(@PathVariable ObjectId id) {
        stationService.delete(id);
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("hasAuthority('ADMIN, EMPLOYEE')")
    @PostMapping("/{id}/add-vehicle/{VehicleSerial}")
    public ResponseEntity<?> addVehicleToStationBySerial(@PathVariable ObjectId id, @PathVariable String VehicleSerial) {
        return ResponseEntity.ok(stationService.save(stationService.getById(id)
                        .addVehicleStatusId(vehicleService.getBySerial(VehicleSerial).toVehicleStatusId())
        ));
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}/remove-vehicle/{vehicleId}")
    public ResponseEntity<?> removeVehicleFromStation(@PathVariable ObjectId id, @PathVariable ObjectId vehicleId) {
        return ResponseEntity.ok(
                stationService.save(stationService.getById(id)
                                .removeVehicleStatusId(vehicleId)
        ));
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> getStationByName(@PathVariable String name) {
        try{
            return ResponseEntity.ok(stationService.getByName(name));
        }catch(Error e){
            return ResponseEntity.badRequest().body("Station not found");
        }
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> addStation(@RequestBody StationDTO dto) {
        try {
            Station station = stationService.add(
                    Station.builder()
                            .id(new ObjectId())
                            .name(dto.name())
                            .capacity(dto.capacity())
                            .vehicleStatusIds(dto.vehicleStatusIdList())
                            .status(dto.status())
                            .coordinates(dto.coordinates())
                            .build()
            );
            return ResponseEntity.created(new URI("/api/stations/" + station.getId())).body(station);
        } catch (URISyntaxException e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStationById(@PathVariable ObjectId id) {
        try {
            return ResponseEntity.ok(stationService.getById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Station not found");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> all() {
        return ResponseEntity.ok(stationService.all());
    }



    @GetMapping("/all/geojson")
    public ResponseEntity<?> allStations() {
        return ResponseEntity.ok(
                new GeoJsonStationCollection(
                        stationService.all()
                                .stream()
                                .map(Station::toGeoJsonStation)
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
