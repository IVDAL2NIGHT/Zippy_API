package com.zippy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class UpdateDTO {
    @NotBlank
    private String newUsername;
    @NotBlank
    private String newEmail;
}
