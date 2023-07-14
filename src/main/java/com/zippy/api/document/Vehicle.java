package com.zippy.api.document;

import com.zippy.api.constants.VehicleStatus;
import com.zippy.api.constants.VehicleType;
import com.zippy.api.models.Maintenance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.bind.annotation.RequestMapping;

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
@RequiredArgsConstructor
@Accessors(chain = true)
public class Vehicle {
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    private String serial;
    @Indexed(unique = true)
    private String gpsSerial;
    private String model;
    private VehicleType type;
    private VehicleStatus status;
    private int Kilometers;
    private LocalDateTime startUpDate;
    private boolean isElectric;
    private int battery;
    private List<Maintenance> maintenances;
}

