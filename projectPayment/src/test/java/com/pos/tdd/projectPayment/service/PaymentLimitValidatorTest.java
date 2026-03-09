package com.pos.tdd.projectPayment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.pos.tdd.projectPayment.exceptions.PaymentLimitException;
import com.pos.tdd.projectPayment.validator.PaymentLimitValidator;

public class PaymentLimitValidatorTest {
	
	 private static final BigDecimal MAX_LIMIT = new BigDecimal("2000.00");
	
	@Test
	void shouldAcceptAmountBelowLimit() {
		
		// arrange
		BigDecimal amount = new BigDecimal("500.00");
		
		//act
		boolean isWithnLimit = PaymentLimitValidator.isWithinLimit(amount);
		
		// assert
		assertThat(isWithnLimit).isTrue();
	}
	
	@Test
	void shouldNotAcceptNullAmount() {
		boolean isWithnLimit = PaymentLimitValidator.isWithinLimit(null);
		assertThat(isWithnLimit).isFalse();
	}
	
	@Test
	void shouldNotAcceptAmountGreaterLimit() {
		boolean isWithnLimit = PaymentLimitValidator.isWithinLimit(new BigDecimal("2500.00"));
		assertThat(isWithnLimit).isFalse();
	}
	
	@Test
	void shouldNotAcceptZeroAmount() {
		assertThatThrownBy(() -> PaymentLimitValidator.isWithinLimit(BigDecimal.ZERO))
							.isInstanceOf(PaymentLimitException.class)
							.hasMessage("Only positive values greater than zero can be accepted!");
	}
	
	@Test
	void shouldNotAcceptNegativeAmount() {
		assertThatThrownBy(() -> PaymentLimitValidator.isWithinLimit(BigDecimal.ZERO))
							.isInstanceOf(PaymentLimitException.class)
							.hasMessage("Only positive values greater than zero can be accepted!");
	}
	
	@Test
	void shouldAcceptLimitAmountMinusOneCent() {
		BigDecimal amount = MAX_LIMIT.subtract(new BigDecimal("0.01"));
		
		boolean isInLimit = PaymentLimitValidator.isWithinLimit(amount);
		
		assertThat(isInLimit).isTrue();
	}
	
	@Test
	void shouldAcceptAmountInLimit() {
		BigDecimal amount = MAX_LIMIT;
		
		boolean isInLimit = PaymentLimitValidator.isWithinLimit(amount);
		
		assertThat(isInLimit).isTrue();
	}
	
	@Test
	void shouldNotAcceptAmountAddOneCent() {
		BigDecimal amount = MAX_LIMIT.add(new BigDecimal("0.01"));
		
		boolean isInLimit = PaymentLimitValidator.isWithinLimit(amount);
		
		assertThat(isInLimit).isFalse();
	}

}
