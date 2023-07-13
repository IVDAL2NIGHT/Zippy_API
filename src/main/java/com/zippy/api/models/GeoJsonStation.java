package com.zippy.api.models;

import com.zippy.api.document.Station;
import lombok.Getter;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

@Getter
public class GeoJsonStation {
    private final String type;
    private final Station properties;
    private final GeoJsonPoint geometry;

    public GeoJsonStation(Station station) {
        this.type = "Feature";
        this.properties = station;
        this.geometry = station.getLocation();
    }
}
