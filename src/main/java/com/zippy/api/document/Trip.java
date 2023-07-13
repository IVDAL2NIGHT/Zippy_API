package com.zippy.api.document;

import com.zippy.api.constants.TripStatus;
import com.zippy.api.models.Report;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trip {

    @Id
    private  ObjectId id;
    private ObjectId userId;
    private ObjectId vehicleId;
    private ObjectId startStationId;
    private ObjectId endStationId;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal cost;

    private TripStatus status;
    private LocalDateTime reservedDate;
    private LocalDateTime deadLine;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Report report;
    private int userRating;
    private String userComment;


    public Trip(ObjectId userId, ObjectId vehicleId, ObjectId startStationId, ObjectId endStationId, LocalDateTime startDate, TripStatus status,  BigDecimal cost, LocalDateTime deadLine) {
        this.id = userId;
        this.vehicleId = vehicleId;
        this.startStationId = startStationId;
        this.endStationId = endStationId;
        this.cost = cost;
        this.status = status;
        this.deadLine = deadLine;
        this.startDate = startDate;
    }
}
