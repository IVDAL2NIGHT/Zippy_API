package com.zippy.api.models.geoJsonResponse;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true, fluent = true)
public class GeoJsonResponseWraper {
    private FeatureCollection featureCollection;
    private int statusCode;
    private String statusMessage;
}
