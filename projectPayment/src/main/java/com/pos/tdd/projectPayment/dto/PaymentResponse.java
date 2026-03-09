package com.pos.tdd.projectPayment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

import com.pos.tdd.projectPayment.model.enums.PaymentSource;
import com.pos.tdd.projectPayment.model.enums.PaymentStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long id;
    private UUID payerId;
    private PaymentSource paymentSource;
    private BigDecimal amount;
    private PaymentStatus status;
}
