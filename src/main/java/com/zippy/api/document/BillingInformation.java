package com.zippy.api.document;

import com.zippy.api.models.Card;
import com.zippy.api.models.Transaction;
import com.zippy.api.models.Wallet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

import static org.springframework.data.mongodb.core.mapping.FieldType.DECIMAL128;

@Document
@Data
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class BillingInformation {
    @Id
    private ObjectId id;
    private List<Card> cards;
    @NotNull
    @Field(targetType = DECIMAL128)
    private BigDecimal balance;
    private List<Transaction> transactions;

    public BillingInformation addCard(Card card) {
        this.cards.add(card);
        return this;
    }

    public BillingInformation addBalance(BigDecimal balance) {
        this.balance = this.balance.add(balance);
        return this;
    }

    public BillingInformation addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        return this;
    }

    public BillingInformation removeCard(Card card) {
        this.cards.remove(card);
        return this;
    }

    public BillingInformation removeBalance(BigDecimal balance) {
        this.balance = this.balance.subtract(balance);
        return this;
    }
}
