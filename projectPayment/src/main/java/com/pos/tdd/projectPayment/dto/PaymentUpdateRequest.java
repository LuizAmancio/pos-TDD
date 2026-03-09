package com.pos.tdd.projectPayment.dto;

import com.pos.tdd.projectPayment.model.enums.PaymentStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentUpdateRequest {

    @NotNull(message = "Status is required")
    private PaymentStatus status;
}
