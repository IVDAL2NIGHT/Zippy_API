package com.zippy.api.document;

import com.zippy.api.constants.DocumentType;
import com.zippy.api.models.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

@Document
@Data
@AllArgsConstructor
public class Employee {
    @Id
    private ObjectId credentialsId;
    private String name;
    private String lastname;
    private int code;
    private LocalDateTime birthdate;
    private String occupation;
    @Email(message = "Email invalido")
    private String email;
    private String phone;
    private String document;
    private DocumentType documentType;
    private Address address;
    private DecimalFormat salary;
}