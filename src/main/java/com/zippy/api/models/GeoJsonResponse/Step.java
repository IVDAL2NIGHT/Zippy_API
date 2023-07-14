package com.zippy.api.models.GeoJsonResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
@Getter
public class Step {
    @JsonProperty("distance")
    private Double distance;
    @JsonProperty("duration")
    private Double duration;
    @JsonProperty("type")
    private int type;
    @JsonProperty("instruction")
    private String instruction;
    @JsonProperty("name")
    private String name;
    @JsonProperty("way_points")
    private Double[] way_points;

}
