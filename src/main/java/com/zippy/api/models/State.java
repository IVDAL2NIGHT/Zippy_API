package com.zippy.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
@Accessors(fluent = false, chain = true)
public class State {
    @NotNull
    private int id;
    @NotNull
    private String name;
}
