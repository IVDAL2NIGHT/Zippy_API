package com.zippy.api.document;

import com.zippy.api.constants.DocumentType;
import com.zippy.api.models.Address;
import com.zippy.api.models.BackupPerson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document
@Data
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class User {
    @Id
    private ObjectId id;
    private String firstName;
    private String lastName;
    private LocalDateTime birthDate;
    private String occupation;
    private String email;
    private String phone;
    private String document;
    private DocumentType documentType;
    private ObjectId billingInformationId;
    private BackupPerson backupPerson;
    private Address address;
}
