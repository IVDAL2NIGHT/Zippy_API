package com.zippy.api.dto;


import com.zippy.api.constants.VehicleStatus;
import com.zippy.api.constants.VehicleType;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class VehicleDTO {
    @NotNull
    private VehicleType type;
    private String model;
    @NotBlank
    private String serial;
    @NotBlank
    private String gpsSerial;
    private VehicleStatus status;
    @NotNull
    private boolean isElectric;
    @Min(0)
    @Max(100)
    private int battery;
}
