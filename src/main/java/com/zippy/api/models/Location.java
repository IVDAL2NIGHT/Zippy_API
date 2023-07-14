package com.zippy.api.models;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@RequiredArgsConstructor
@Accessors(fluent = false, chain = true)
public class Location {
    private double latitude;
    private double longitude;
}
