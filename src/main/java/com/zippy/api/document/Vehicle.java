package com.zippy.api.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.zippy.api.constants.VehicleStatus;
import com.zippy.api.constants.VehicleType;
import com.zippy.api.models.Maintenance;
import com.zippy.api.models.VehicleStatusId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Vehicle
 * <p>
 * This class is the representation of a vehicle in the database.
 *
 * @version 1.0
 * @see VehicleType
 * @see VehicleStatus
 * @see Maintenance
 * @see com.zippy.api.repository.VehicleRepository
 * @since 1.0
 */
@Document
@Data
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class Vehicle {
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    private String serial;
    @Indexed(unique = true)
    @JsonIgnore
    private String gpsSerial;
    private String model;
    private VehicleType type;
    private VehicleStatus status;
    private int kilometers;
    private LocalDateTime startUpDate;
    private boolean isElectric;
    private int battery;
    private List<Maintenance> maintenances;


    public VehicleStatusId toVehicleStatusId() {
        return VehicleStatusId.builder()
                ._id(this.id)
                .status(this.status)
                .type(this.type)
                .build();
    }
}

