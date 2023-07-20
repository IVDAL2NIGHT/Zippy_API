package com.zippy.api.dto;

import com.zippy.api.constants.VehicleStatus;
import com.zippy.api.constants.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Accessors(fluent = true, chain = false)
@Getter
public class VehicleDTO {
    private VehicleType type;
    private String model;
    @NotBlank
    private String serial;
    @NotBlank
    private String gpsSerial;
    private VehicleStatus status;
    @NotBlank
    private boolean isElectric;
    private int battery;
}
