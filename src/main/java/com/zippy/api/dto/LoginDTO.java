package com.zippy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@Accessors(fluent = true)

public class LoginDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}