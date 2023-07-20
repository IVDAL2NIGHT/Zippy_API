package com.zippy.api.models;

import com.zippy.api.constants.VehicleStatus;
import com.zippy.api.constants.VehicleType;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.Id;

@Data
@Builder
@Accessors(fluent = false, chain = true)
public class VehicleStatusId {
    @Id
    @Lazy
    private ObjectId _id;
    private VehicleStatus status;
    private VehicleType type;
}
