package com.zippy.api.models.GeoJsonResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
@Getter
public class Summary {
    @JsonProperty("distance")
    private Double distance;
    @JsonProperty("duration")
    private Double duration;
}
