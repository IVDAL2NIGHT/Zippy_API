package com.zippy.api.models;

import com.zippy.api.document.Station;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class GeoJsonStationProperties {
    private Station station;
}
