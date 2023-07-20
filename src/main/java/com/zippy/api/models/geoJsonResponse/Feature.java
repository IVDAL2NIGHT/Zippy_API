package com.zippy.api.models.geoJsonResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;


@Builder
@Accessors(chain = true, fluent = true)
@Getter
public class Feature {
    @JsonProperty("type")
    private String type;
    @JsonProperty("bbox")
    private Double[] bbox;
    @JsonProperty("geometry")
    private Geometry geometry;
    @JsonProperty("properties")
    private Properties properties;
}
