package com.zippy.api.document;

import com.zippy.api.constants.DocumentType;
import com.zippy.api.models.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import java.math.BigDecimal;
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
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal salary;
}
