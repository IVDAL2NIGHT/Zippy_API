package com.zippy.api.dto;

import com.zippy.api.models.Card;
import com.zippy.api.models.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class UpdateBillingInformationDTO {
    @NotBlank
    private ObjectId userId;
    @NotBlank
    private ObjectId billingInformationId;
    private Card newCard;
    private BigDecimal money;
    private Transaction transaction;
}
