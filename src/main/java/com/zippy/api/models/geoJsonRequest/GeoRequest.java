package com.zippy.api.models.geoJsonRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true, fluent = true)
public class GeoRequest {
    private Double[][] coordinates;
    private Boolean elevation;
    private String instructions_format;
    private String[] extra_info;
    private String language;
    private String units;
    private String preference;
    private Boolean geometry;

    public String toJson() {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\"coordinates\":[");
        for (int i = 0; i < coordinates.length; i++) {
            Double[] point = coordinates[i];
            jsonBuilder.append("[").append(point[0]).append(",").append(point[1]).append("]");
            if (i < coordinates.length - 1) {
                jsonBuilder.append(",");
            }
        }
        jsonBuilder.append("],\"elevation\":\"").append(elevation).append("\",\"language\":\"").append(language)
                .append("\",\"preference\":\"").append(preference).append("\",\"units\":\"").append(units)
                .append("\",\"geometry\":").append(geometry).append("}");

        return jsonBuilder.toString();
    }
}
