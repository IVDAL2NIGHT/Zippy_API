package com.zippy.api.document;

import com.zippy.api.models.Card;
import com.zippy.api.models.Wallet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
public class BillingInformation {
    @Id
    private ObjectId id;
    private List<Card> cards;
    private Wallet wallet;

}
