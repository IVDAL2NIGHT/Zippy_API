package com.zippy.api.dto;

import com.zippy.api.models.Address;
import com.zippy.api.models.City;
import com.zippy.api.models.Country;
import com.zippy.api.models.State;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class updateUserDTO {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String phone;
    @NotBlank
    private Address address;
    @NotBlank
    private String occupation;
}
