package com.zippy.api.models;

import com.zippy.api.constants.VehicleStatus;
import lombok.*;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.Id;

@Data
@RequiredArgsConstructor
@Accessors(fluent = false, chain = true)
public class VehicleStatusId {
    @Id
    @Lazy
    private ObjectId _id;
    private VehicleStatus status;
}
