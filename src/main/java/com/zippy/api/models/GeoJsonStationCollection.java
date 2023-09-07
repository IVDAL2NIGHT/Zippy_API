package com.zippy.api.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

@Getter
@Builder
@AllArgsConstructor
@Accessors(fluent = false, chain = true)
public class GeoJsonStationCollection{
    private final String type;
    private final GeoJsonStation[] features;

    public GeoJsonStationCollection(GeoJsonStation[] features) {
        this.type = "FeatureCollection";
        this.features = features;
    }

    public int size() {
        return features.length;
    }
}
