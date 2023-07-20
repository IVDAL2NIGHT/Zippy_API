package com.zippy.api.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zippy.api.constants.StationStatus;
import com.zippy.api.models.VehicleStatusId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@Data
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class Station {
    @Id
    private ObjectId id;
    private String name;
    @JsonIgnore
    private Double[] coordinates;
    private int capacity;
    private List<VehicleStatusId> vehicleStatusIds;
    private StationStatus status;

    public Station addVehicleStatusId(VehicleStatusId vehicleStatusId) {
        this.vehicleStatusIds.add(vehicleStatusId);
        return this;
    }

    public Station removeVehicleStatusId(ObjectId VehicleId) {
        this.vehicleStatusIds = this.vehicleStatusIds.stream().filter(vehicleStatusId -> !vehicleStatusId.get_id().equals(VehicleId)).toList();
        return this;
    }

}
