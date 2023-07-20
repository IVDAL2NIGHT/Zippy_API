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
public class GeoJsonStationCollection implements Iterable<GeoJsonStation> {
    private final String type;
    private final GeoJsonStation[] features;

    public GeoJsonStationCollection(GeoJsonStation[] stations) {
        this.type = "FeatureCollection";
        this.features = stations;
    }

    @NotNull
    @Override
    public Iterator<GeoJsonStation> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < features.length;
            }

            @Override
            public GeoJsonStation next() {
                return features[index++];
            }
        };
    }

    public int size() {
        return features.length;
    }
}
