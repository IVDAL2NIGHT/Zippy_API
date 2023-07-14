package com.zippy.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;


@Data
@RequiredArgsConstructor
@Accessors(fluent = false, chain = true)
public class Maintenance {
    @Id
    private ObjectId id;
    @NotNull
    private LocalDateTime date;
    @NotNull
    private String report;
    @NotNull
    private String reason;
    private int kilometers;
    @NotNull
    private ObjectId employeeId;
    private double cost;
}