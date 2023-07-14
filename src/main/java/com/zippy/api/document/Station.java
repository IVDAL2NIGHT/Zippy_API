package com.zippy.api.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zippy.api.constants.StationStatus;
import com.zippy.api.models.VehicleStatusId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
public class Station {
    @Id
    private ObjectId id;
    private String name;
    @JsonIgnore
    private GeoJsonPoint location;
    private int capacity;
    private List<VehicleStatusId> vehicleStatusIds;
    private StationStatus status;

}
