package com.pos.tdd.projectPayment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import com.pos.tdd.projectPayment.model.Payment;
import com.pos.tdd.projectPayment.model.enums.PaymentSource;
import com.pos.tdd.projectPayment.model.enums.PaymentStatus;

@DataJpaTest
public class PaymentRepositoryTest {

	@Autowired
	private PaymentRepository repository;
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	void shoulSumDailyPaymentsByPayerId() {
		UUID payerID = UUID.randomUUID();
		
		var firstPayment = Payment.builder()
			.payerId(payerID)
			.paymentSource(PaymentSource.DEBIT_CARD)
			.amount(new BigDecimal("250.00"))
			.status(PaymentStatus.PENDING)
		.build();
		
		var secondPayment = Payment.builder()
				.payerId(payerID)
				.paymentSource(PaymentSource.DEBIT_CARD)
				.amount(new BigDecimal("500.00"))
				.status(PaymentStatus.PENDING)
			.build();
		
		repository.saveAll(List.of(firstPayment, secondPayment));
		
		LocalDate today = LocalDate.now();
		LocalDateTime startDay = today.atStartOfDay();
		LocalDateTime endDay = today.plusDays(1).atStartOfDay();
		
		var total = repository.sumPaymentsByPayerIdAndDate(payerID, startDay, endDay);
		
		assertThat(total).isEqualByComparingTo(new BigDecimal("750.00"));
	}
	
	@Test
	void shoulNotSumPaymentsFromDifferentDays() {
		UUID payerID = UUID.randomUUID();
		
		var firstPayment = Payment.builder()
				.payerId(payerID)
				.paymentSource(PaymentSource.DEBIT_CARD)
				.amount(new BigDecimal("250.00"))
				.status(PaymentStatus.PENDING)
				.build();
		
		var secondPayment = Payment.builder()
				.payerId(payerID)
				.paymentSource(PaymentSource.DEBIT_CARD)
				.amount(new BigDecimal("500.00"))
				.status(PaymentStatus.PENDING)
				.build();
			
		repository.saveAll(List.of(firstPayment, secondPayment));
		entityManager.flush();
		
		LocalDate today = LocalDate.now();
		LocalDateTime startDay = today.atStartOfDay();
		LocalDateTime endDay = today.plusDays(1).atStartOfDay();
		
		LocalDateTime yesterday = startDay.minusDays(1).plusHours(10);
		
		/*
		 * necessário o EntityManager para contornarmos o set automatico da data de criação
		 * para assim podermos testar se não esta somando com o dia anterior
		 */
		entityManager.getEntityManager()
					 	.createQuery("UPDATE Payment p SET p.createdAt = :createdAt WHERE p.id = :id")
					 	.setParameter("createdAt", yesterday)
					 	.setParameter("id", firstPayment.getId())
					 	.executeUpdate();
		
		entityManager.clear();
		
		var total = repository.sumPaymentsByPayerIdAndDate(payerID, startDay, endDay);
		
		assertThat(total).isEqualByComparingTo(new BigDecimal("500.00"));
		
	}
	
}
