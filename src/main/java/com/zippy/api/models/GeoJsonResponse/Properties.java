package com.zippy.api.models.GeoJsonResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
@Getter
public class Properties {
    @JsonProperty("ascent")
    private Double ascent;
    @JsonProperty("descent")
    private Double descent;
    @JsonProperty("transfers")
    private int transfers;
    @JsonProperty("fare")
    private int fare;
    @JsonProperty("segments")
    private Segments segments[];
    @JsonProperty("summary")
    private Summary summary;
}
