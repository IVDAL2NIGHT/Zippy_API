package com.zippy.api.models;

import com.zippy.api.document.Station;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@RequiredArgsConstructor
@Accessors(fluent = false, chain = true)
public class GeoJsonStation {
    private final String type;
    private final GeoJsonStationProperties properties;
    private final Double[] geometry;

    public GeoJsonStation(Station station) {
        this.type = "Feature";
        this.properties = new GeoJsonStationProperties(station);
        this.geometry = station.getCoordinates();
    }
}
