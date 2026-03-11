package com.pos.tdd.projectPayment.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.tdd.projectPayment.dto.PaymentRequest;
import com.pos.tdd.projectPayment.dto.PaymentResponse;
import com.pos.tdd.projectPayment.model.Payment;
import com.pos.tdd.projectPayment.model.enums.PaymentSource;
import com.pos.tdd.projectPayment.model.enums.PaymentStatus;
import com.pos.tdd.projectPayment.repository.PaymentRepository;

/*
 * Testes de integração com testContainers
 */

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("mysql")
class PaymentIntegrationWithTestContainers {
	
	private static final String BASE_URL_PAYMENT = "/api/payments"; 
	private static final String URL_PAYMENT_BY_ID = BASE_URL_PAYMENT + "/{paymentId}"; 
	private static final String URL_PAYMENTS_BY_PAYER_ID = BASE_URL_PAYMENT + "/payer/{payerId}"; 
	
	@Autowired
	protected MockMvc mockMVC;
	@Autowired
	protected PaymentRepository repository;
	
	private final ObjectMapper mapper = new ObjectMapper();
	
	/*
	 *  Sem profile:
	 
	@Container
	private final static MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:9.2.0"); 
	*/
	
	// metodo para salvar payment utilizado nos testes
	private Payment addPayment() {
		/*
		 * Criamos o objeto, já que vai verificar se o pagamento existe
		 * Usado a Entidade e não o PaymentRequest, pois é necessário salvar
		 */
		var payment = Payment.builder()
				.payerId(UUID.randomUUID())
				.paymentSource(PaymentSource.PIX)
				.amount(BigDecimal.valueOf(200.75))
				.status(PaymentStatus.PENDING)
				.build();
		
		/*
		 * Salvamos o pagamento para ter certeza que teremos o pagamento
		 */
		return repository.save(payment);
	}
	
	@BeforeEach
	void cleandatabase() {
		repository.deleteAll();
	}
	
	
	@Test
	void getPaymentById() throws Exception {
		
		
		var savedPayment = addPayment();
		
		/*
		 * perform -> cria internamente um request builder no Spring e permite executar requisições
		 * O paramétro do perform será o método HTTP que executaremos -> esta na classe MockMvcRequestBuilders
		 * andExpect -> valida o status 
		 * 	e cada um dos atributos retornados(utilizando jsonPath juntamento com um Matcher para verificar valor)
		 * 
		 * $ -> ponto de partida das expressões do JsonPath que se refere ao objeto ou array JSON
		 */
		mockMVC.perform(get(URL_PAYMENT_BY_ID, savedPayment.getId()))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.payerId", is(savedPayment.getPayerId().toString())))
		.andExpect(jsonPath("$.paymentSource", is(savedPayment.getPaymentSource().name())))
		.andExpect(jsonPath("$.amount", is(savedPayment.getAmount().doubleValue())))
		.andExpect(jsonPath("$.status", is(savedPayment.getStatus().name())));
		
	}
	
	@Test
	void getAllPayments() throws Exception {
		
		var savedPayment1 = addPayment();
		var savedPayment2 = addPayment();
		
		mockMVC.perform(get(BASE_URL_PAYMENT))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[*]", notNullValue()))
				.andExpect(jsonPath("$[0].payerId", is(savedPayment1.getPayerId().toString())))
				.andExpect(jsonPath("$[0].paymentSource", is(savedPayment1.getPaymentSource().name())))
				.andExpect(jsonPath("$[0].amount", is(savedPayment1.getAmount().doubleValue())))
				.andExpect(jsonPath("$[0].status", is(savedPayment1.getStatus().name())))
				.andExpect(jsonPath("$[1].payerId", is(savedPayment2.getPayerId().toString())))
				.andExpect(jsonPath("$[1].paymentSource", is(savedPayment2.getPaymentSource().name())))
				.andExpect(jsonPath("$[1].amount", is(savedPayment2.getAmount().doubleValue())))
				.andExpect(jsonPath("$[1].status", is(savedPayment2.getStatus().name())));
		
	}
	
	@Test
	void getAllPaymentsWithObjectMapper() throws Exception {
		
		var savedPayment1 = addPayment();
		var savedPayment2 = addPayment();
		
		String response = mockMVC.perform(get(BASE_URL_PAYMENT))
			.andExpect(status().isOk()).andReturn()
			.getResponse()
			.getContentAsString();
		
		List<PaymentResponse> listPayments = mapper
				.readValue(
							response, 
							mapper.getTypeFactory()
								  .constructCollectionType(List.class,PaymentResponse.class)
						);
		
		
		assertThat(listPayments).isNotNull().hasSize(2);
		assertThat(listPayments.get(0).getPayerId()).isEqualTo(savedPayment1.getPayerId());
		assertThat(listPayments.get(1).getPayerId()).isEqualTo(savedPayment2.getPayerId());
		
		/*
		.andExpect(jsonPath("$", hasSize(2)))
		.andExpect(jsonPath("$[*]", notNullValue()))
		.andExpect(jsonPath("$[0].payerId", is(savedPayment1.getPayerId().toString())))
		.andExpect(jsonPath("$[0].paymentSource", is(savedPayment1.getPaymentSource().name())))
		.andExpect(jsonPath("$[0].amount", is(savedPayment1.getAmount().doubleValue())))
		.andExpect(jsonPath("$[0].status", is(savedPayment1.getStatus().name())))
		.andExpect(jsonPath("$[1].payerId", is(savedPayment2.getPayerId().toString())))
		.andExpect(jsonPath("$[1].paymentSource", is(savedPayment2.getPaymentSource().name())))
		.andExpect(jsonPath("$[1].amount", is(savedPayment2.getAmount().doubleValue())))
		.andExpect(jsonPath("$[1].status", is(savedPayment2.getStatus().name())));
		*/
		
	}
	
	@Test
	void getPaymentsByPayerId() throws Exception {
		
		var savedPayment = addPayment();
		
		
		/*
		 * perform -> cria internamente um request builder no Spring e permite executar requisições
		 * O paramétro do perform será o método HTTP que executaremos -> esta na classe MockMvcRequestBuilders
		 * andExpect -> valida o status 
		 * 	e cada um dos atributos retornados(utilizando jsonPath juntamento com um Matcher para verificar valor)
		 * 
		 * $ -> ponto de partida das expressões do JsonPath que se refere ao objeto ou array JSON
		 * $[*] -> Verificar se o valor existe em qualquer lugar da lista
		 * $.length() -> tamanho da lista
		 */
		mockMVC.perform(get(URL_PAYMENTS_BY_PAYER_ID, savedPayment.getPayerId()))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].payerId", is(savedPayment.getPayerId().toString())))
		.andExpect(jsonPath("$[0].paymentSource", is(savedPayment.getPaymentSource().name())))
		.andExpect(jsonPath("$[0].amount", is(savedPayment.getAmount().doubleValue())))
		.andExpect(jsonPath("$[0].status", is(savedPayment.getStatus().name())));
		
	}
	
	@Test
	void createPaymentById() throws Exception {
		
		String payload = """
				{
					"payerId": "550e8400-e29b-41d4-a716-446655440000",
					"paymentSource": "PIX",
					"amount": 100.50
				}
				""";
		
		mockMVC.perform(post(BASE_URL_PAYMENT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.payerId", is("550e8400-e29b-41d4-a716-446655440000")))
		.andExpect(jsonPath("$.paymentSource", is(PaymentSource.PIX.name())))
		.andExpect(jsonPath("$.amount", is(100.50)))
		.andExpect(jsonPath("$.status", is(PaymentStatus.PENDING.name())));
	}
	
	@Test
	void createPaymentByIdWithObjectMapper() throws Exception {
		
		var paymentRequest = PaymentRequest.builder()
				.payerId(UUID.randomUUID())
				.paymentSource(PaymentSource.PIX)
				.amount(BigDecimal.valueOf(100.50))
				.build();
		
		/*
		 * Retorna o resultado como String
		 */
		String response = mockMVC.perform(post(BASE_URL_PAYMENT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(paymentRequest)))
			.andExpect(status().isCreated())
			.andReturn()
			.getResponse()
			.getContentAsString();
		
		/*
		 * Mapeamos a resposta String para a classe
		 */
		var payment = mapper.readValue(response, PaymentResponse.class);
		
		/*
		 * Asserções com AssertJ
		 */
		assertThat(payment).isNotNull();
		assertThat(payment.getPayerId()).isEqualTo(paymentRequest.getPayerId());
		assertThat(payment.getAmount()).isEqualTo(paymentRequest.getAmount());
		assertThat(payment.getPaymentSource()).isEqualTo(paymentRequest.getPaymentSource());
		assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PENDING);
		
	}
	
	@Test
	void getNotExistsPaymentId() throws Exception {
		String id = "555";
		
		mockMVC.perform(get(URL_PAYMENT_BY_ID, id))
		.andExpect(status().isNotFound())
		.andExpect(content().string("Payment not found with ID: "+id));
	}
	
	@Test
	void updateExistsPaymentId() throws Exception {
		
		String payload = """
				{
					"status": "FRAUD"
				}
				""";
		
		var savedPayment = addPayment();
		
		mockMVC.perform(put(URL_PAYMENT_BY_ID, savedPayment.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.payerId", is(savedPayment.getPayerId().toString())))
		.andExpect(jsonPath("$.paymentSource", is(savedPayment.getPaymentSource().name())))
		.andExpect(jsonPath("$.amount", is(savedPayment.getAmount().doubleValue())))
		.andExpect(jsonPath("$.status", is(PaymentStatus.FRAUD.name())));
		
	}

}
