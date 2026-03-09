package com.pos.tdd.projectPayment.validator;

import java.math.BigDecimal;

import com.pos.tdd.projectPayment.exceptions.PaymentLimitException;

public final class PaymentLimitValidator {

    private static final BigDecimal MAX_LIMIT = new BigDecimal("2000.00");

    public static boolean isWithinLimit(BigDecimal amount) {
        try {
        	if (amount == null) return false; 
        	
        	if (amount.compareTo(BigDecimal.ZERO) <= 0) {
        		throw new PaymentLimitException("Only positive values greater than zero can be accepted!");
        	}
        	
        	return amount.compareTo(MAX_LIMIT) <= 0;  
        	
        }
        catch (Exception e) {
			throw new PaymentLimitException(e.getMessage());
		}
    }
}
