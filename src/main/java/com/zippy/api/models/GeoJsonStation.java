package com.zippy.api.models;

import com.zippy.api.document.Station;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = false, chain = true)
public class GeoJsonStation {
    private final String type;
    private final Station properties;
    private final Double[] geometry;

    public GeoJsonStation(Station station) {
        this.type = "Station";
        this.properties = station;
        this.geometry = station.getCoordinates();
    }
}
