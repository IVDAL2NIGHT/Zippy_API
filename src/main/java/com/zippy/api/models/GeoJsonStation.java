package com.zippy.api.models;

import com.zippy.api.document.Station;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

@Getter
@RequiredArgsConstructor
@Accessors(fluent = false, chain = true)
public class GeoJsonStation{
    private final String type;
    private final Station properties;
    private final GeoJsonPoint geometry;

    public GeoJsonStation(Station properties) {
        this.type = "Feature";
        this.properties = properties;
        this.geometry = properties.getLocation();
    }
}
