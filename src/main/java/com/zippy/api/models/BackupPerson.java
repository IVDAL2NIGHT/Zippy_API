package com.zippy.api.models;

import com.zippy.api.constants.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class BackupPerson {
    @NotNull
    private String firtName;
    @NotNull
    private String lastName;
    @NotNull
    private String phone;
    @NotNull
    private String email;
    private String document;
    private DocumentType documentType;
}
