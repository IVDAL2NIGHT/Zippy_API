package com.zippy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public class UpdateDTO {
    @NotBlank
    private final String newUsername;
    @NotBlank
    private final String newEmail;
}
