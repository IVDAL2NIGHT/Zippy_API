package com.zippy.api.document;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document
@Data
public class RefreshToken {
    @Id
    private ObjectId id;
    @DocumentReference(lazy = true)
    private Credential owner;
}