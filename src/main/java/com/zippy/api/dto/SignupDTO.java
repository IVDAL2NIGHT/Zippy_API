package com.zippy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@Accessors(fluent = true)

public class SignupDTO {
    @NotNull
    private CredentialDTO credential;
    @NotNull
    private UserDTO user;
}