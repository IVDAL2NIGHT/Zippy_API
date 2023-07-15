package com.zippy.api.models.geoJsonResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@Accessors(chain = true, fluent = true)
@Getter
public class FeatureCollection {
    @JsonProperty("type")
    private String type;
    @JsonProperty("features")
    private Feature[] features;
    @JsonProperty("bbox")
    private Double[] bbox;
}
