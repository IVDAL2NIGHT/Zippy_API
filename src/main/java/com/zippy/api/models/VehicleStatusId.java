package com.zippy.api.models;

import com.zippy.api.constants.VehicleStatus;
import com.zippy.api.constants.VehicleType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Data
@Builder
@Accessors(fluent = false, chain = true)
public class VehicleStatusId {
    @DocumentReference
    private ObjectId id;
    private VehicleStatus status;
    private VehicleType type;
}
