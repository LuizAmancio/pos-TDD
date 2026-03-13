package com.pos.tdd.projectPayment.dataDriven.dataFactory;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.UUID;

import com.pos.tdd.projectPayment.dto.PaymentRequest;
import com.pos.tdd.projectPayment.model.enums.PaymentSource;

import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;

@Slf4j
public final class PaymentDataFactory {
	
	private static final Faker faker = new Faker(Locale.of("pt-BR"));
	
	private PaymentDataFactory() {}
	
	public static PaymentRequest validPaymentRequest() {
		var paymentRequest = basePaymentRequest();
		
		log.info("Creating valid payment request : {}", paymentRequest);
		return paymentRequest;
	}
	
	public static PaymentRequest invalidPaymentRequest() {
		var paymentRequest = basePaymentRequest();
		
		paymentRequest.setAmount(BigDecimal.valueOf(faker.number().numberBetween(2001, 3000)));
		
		log.info("Creating invalid payment request : {}", paymentRequest);
		return paymentRequest;
	}
	
	private static PaymentRequest basePaymentRequest() {
		return PaymentRequest.builder()
				.payerId(UUID.randomUUID())
				.paymentSource(faker.options().option(PaymentSource.class)) // pega o enum randomicamente
				.amount(BigDecimal.valueOf(faker.number().numberBetween(1, 2000)))
				.build();
	}

}
