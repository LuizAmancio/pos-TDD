package com.pos.tdd.projectPayment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.pos.tdd.projectPayment.exceptions.PaymentLimitException;
import com.pos.tdd.projectPayment.validator.PaymentLimitValidator;

class PaymentLimitValidatorTest {
	
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
	
	// Utilizando Data Driven
	
	static Stream<Arguments> edgeCasesForLimit(){
		return Stream.of(
					Arguments.arguments(new BigDecimal("2000.01")),
					Arguments.arguments(new BigDecimal("3500.00"))
				);
	}
	
	static Stream<Arguments> happyPathsForLimit(){
		return Stream.of(
				Arguments.arguments(new BigDecimal("00.1")),
				Arguments.arguments(new BigDecimal("1999.99")),
				Arguments.arguments(new BigDecimal("2000.00"))
				);
	}
	
	static Stream<Arguments> failedPaths(){
		return Stream.of(
				Arguments.arguments(new BigDecimal("00.0")),
				Arguments.arguments(new BigDecimal("-1999.99"))
				);
	}
	
	@ParameterizedTest
	@MethodSource("edgeCasesForLimit")
	void edge(BigDecimal amount) {
		
		boolean isInLimit = PaymentLimitValidator.isWithinLimit(amount);
		assertThat(isInLimit).isFalse();
		
	}
	
	@ParameterizedTest
	@MethodSource("happyPathsForLimit")
	void happyPaths(BigDecimal amount) {
		
		boolean isInLimit = PaymentLimitValidator.isWithinLimit(amount);
		assertThat(isInLimit).isTrue();
		
	}
	
	@ParameterizedTest
	@MethodSource("failedPaths")
	void failedPaths(BigDecimal amount) {
		
		assertThatThrownBy(() -> PaymentLimitValidator.isWithinLimit(amount))
			.isInstanceOf(PaymentLimitException.class);
		
	}

}
