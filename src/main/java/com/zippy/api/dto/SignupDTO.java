package com.zippy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor

public class SignupDTO {
    @NotNull
    private CredentialDTO credential;
    @NotNull
    private UserDTO user;
}