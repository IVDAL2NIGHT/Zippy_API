package com.zippy.api.dto;

import com.zippy.api.constants.DocumentType;
import com.zippy.api.models.Address;
import com.zippy.api.models.BackupPerson;
import com.zippy.api.models.Card;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class UserDTO {
    @NotBlank
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String birthday;
    @NotBlank
    private String occupation;
    @NotBlank
    private String document;
    @NotBlank
    private DocumentType documentType;
    @NotNull
    private Address address;
    @NotBlank
    private String phone;
    @NotNull
    private BackupPerson backupPerson;

}
