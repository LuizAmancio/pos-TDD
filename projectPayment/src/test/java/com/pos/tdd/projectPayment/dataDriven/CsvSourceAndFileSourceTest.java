package com.pos.tdd.projectPayment.dataDriven;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

class CsvSourceAndFileSourceTest {

	private static final String PRECO_MAXIMO = "300.00";
	
	@ParameterizedTest(name = "Produto {0} no valor de {1} deve ser menor que R$" + PRECO_MAXIMO)
	@CsvSource({
		"Micro SD Card 16gb, 56.99" ,
		"JBL GO, 149.99",
		"Ipad Air, 164.99" ,
	})
	void precoMenorQueMaximo(String nomeProduto, BigDecimal valor) {
		
		Assertions.assertThat(nomeProduto).isNotEmpty();
		Assertions.assertThat(valor).isLessThanOrEqualTo(new BigDecimal(PRECO_MAXIMO));
	}
	
	@ParameterizedTest(name = "Produto {0} no valor de R${1} deve ser menor que R$" + PRECO_MAXIMO)
	@CsvFileSource(resources = "/produtos.csv", numLinesToSkip = 1)
	void precoMenorQueMaximoPorFileSource(String nomeProduto, BigDecimal valor) {
		
		Assertions.assertThat(nomeProduto).isNotEmpty();
		Assertions.assertThat(valor).isLessThanOrEqualTo(new BigDecimal(PRECO_MAXIMO));
	}
}
