package com.zippy.api.models;

import com.zippy.api.constants.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
public class VehicleStatusId {
    @Id
    @Lazy
    private ObjectId _id;
    private VehicleStatus status;
}
