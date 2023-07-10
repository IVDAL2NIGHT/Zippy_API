package com.zippy.api.document;

import com.zippy.api.constants.StationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.zippy.api.models.VehicleStatusId;

import java.util.List;

@Document
@Data
@AllArgsConstructor
public class Station {
    @Id
    private ObjectId id;
    private String name;
    private String location;
    private int capacity;
    private List<VehicleStatusId> vehicleStatusIds;
    private StationStatus status;
}
