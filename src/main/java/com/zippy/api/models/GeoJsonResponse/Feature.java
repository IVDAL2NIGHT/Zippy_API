package com.zippy.api.models.GeoJsonResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.geo.GeoJsonLineString;


@RequiredArgsConstructor
@Accessors(chain = true, fluent = true)
@Getter
public class Feature {
    @JsonProperty("type")
    private String type;
    @JsonProperty("bbox")
    private Double bbox[];
    @JsonProperty("geometry")
    private Geometry geometry;
    @JsonProperty("properties")
    private Properties properties;
}
