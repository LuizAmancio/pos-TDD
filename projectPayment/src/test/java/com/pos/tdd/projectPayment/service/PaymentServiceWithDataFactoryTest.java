package com.pos.tdd.projectPayment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pos.tdd.projectPayment.dataDriven.dataFactory.PaymentDataFactory;
import com.pos.tdd.projectPayment.dto.PaymentRequest;
import com.pos.tdd.projectPayment.exceptions.PaymentLimitException;
import com.pos.tdd.projectPayment.model.enums.PaymentSource;
import com.pos.tdd.projectPayment.model.enums.PaymentStatus;
import com.pos.tdd.projectPayment.repository.PaymentRepository;

@ExtendWith(MockitoExtension.class)
class PaymentServiceWithDataFactoryTest {
	
	/*
	 * Anotamos o que iremos simular
	 */
	@Mock
	private PaymentRepository paymentRepository;
	
	/*
	 *  Anotamos a classe sob teste que precisa executar o código, 
		onde usaremos a sua saída para verificar o resultado esperado
	 */
	@InjectMocks
	private PaymentService paymentService;
	
	@Test
	@DisplayName("Should save a payment when limit is not exceeded")
	void shouldSavePaymentWhenAmountIsNotExceedDailyLimit() {
		
		/*
		 *  Mocka o comportamento do método utilizando o Argument Match any(), para salvar pagamento
		 *  
		 *  Passa o primeiro argumento passado ao método save(), 
		 *  onde o thenAnswer() pode retornar respostas dinâmicas
		 */
		when(paymentRepository.save(any())).thenAnswer(in -> in.getArgument(0));
		
		/*
		 * Recebe do data factory
		 */
		var paymentRequest = PaymentDataFactory.validPaymentRequest();
		
		/*
		 * Efetua pagamento e guarda resposta
		 */
		var savedPayment = paymentService.createPayment(paymentRequest);
		
		/*
		 * Garante que o pagamento retornado é o mesmo
		 */		
		assertThat(savedPayment.getPayerId()).isEqualTo(paymentRequest.getPayerId());
		assertThat(savedPayment.getPaymentSource()).isEqualTo(PaymentSource.CREDIT_CARD);
		assertThat(savedPayment.getPayerId()).isEqualTo(paymentRequest.getPayerId());
		assertThat(savedPayment.getAmount()).isEqualByComparingTo(paymentRequest.getAmount());
		assertThat(savedPayment.getStatus()).isEqualTo(PaymentStatus.PENDING);
		
		/*
		 * Verifica se o pagamento foi salvo
		 */
		verify(paymentRepository).save(any());
	}
	
	
	@Test
	@DisplayName("Should not save a payment when limit is exceeded")
	void shouldNotSavePaymentWhenAmountIsExceedDailyLimit() {
		
		/*
		 * Recebe do data factory
		 */
		var paymentRequest = PaymentDataFactory.invalidPaymentRequest();
		
		/*
		 * Garante que é lançado a exceção
		 */		
		assertThatThrownBy(() -> paymentService.createPayment(paymentRequest))
			.isInstanceOf(PaymentLimitException.class)
			.hasMessageContaining("Daily payment limit exceeded for source:");
		
		/*
		 * Verifica que foi chamado o método de soma limite e não foi salvo
		 * 
		 * Quando é passado um Matcher (ex: any()), todos os outros devem ser Matchers também
		 * por isso o uso do 'eq'
		 */
		verify(paymentRepository).sumPaymentsByPayerIdAndDate(eq(paymentRequest.getPayerId()), any(), any());
		verify(paymentRepository, never()).save(any());
	}

}
