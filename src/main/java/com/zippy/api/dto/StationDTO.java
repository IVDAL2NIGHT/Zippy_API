package com.zippy.api.dto;

import com.zippy.api.constants.StationStatus;
import com.zippy.api.models.VehicleStatusId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public class StationDTO {
    @NotBlank
    private String name;
    @NotNull
    private Double[] coordinates;
    @NotBlank
    private int capacity;
    @NotNull
    private StationStatus status;
    private List<VehicleStatusId> vehicleStatusIdList;
}
