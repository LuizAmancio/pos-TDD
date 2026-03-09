package com.pos.tdd.projectPayment.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.pos.tdd.projectPayment.model.enums.PaymentSource;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    @NotNull(message = "Payer ID is required")
    private UUID payerId;

    @NotNull(message = "Payment source is required")
    private PaymentSource paymentSource;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
}
