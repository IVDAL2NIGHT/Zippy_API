package com.zippy.api.document;

import com.zippy.api.constants.StationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@AllArgsConstructor
public class Station {
    @Id
    private ObjectId id;
    private String name;
    private String location;
    private int capacity;
    private List<Vehicle> vehicles;
    private StationStatus status;

    public String getLocation() {
        return location;
    }
}
