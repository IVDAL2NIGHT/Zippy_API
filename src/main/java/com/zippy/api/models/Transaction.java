package com.zippy.api.models;

import com.zippy.api.constants.PaymentMethod;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@Accessors(fluent = false, chain = true)
public class Transaction {
    @NotNull
    private LocalDateTime date;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private PaymentMethod paymentMethod;
}
