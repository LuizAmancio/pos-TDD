package com.pos.tdd.projectPayment.exceptions;

public class PaymentLimitException extends RuntimeException {

    public PaymentLimitException(String message) {
        super(message);
    }
}
