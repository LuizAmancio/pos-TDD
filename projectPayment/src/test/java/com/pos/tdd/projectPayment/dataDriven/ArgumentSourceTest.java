package com.pos.tdd.projectPayment.dataDriven;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import com.pos.tdd.projectPayment.dataDriven.MethodSourceTest.Pessoa;

class ArgumentSourceTest {

	@ParameterizedTest
	@ArgumentsSource(PessoaArgumentSourceData.class)
	void podeVotarObjectTest(Pessoa pessoa, String resultador) {
		Assertions.assertThat(pessoa.getIdade())
		.isGreaterThanOrEqualTo(18)
		.isLessThanOrEqualTo(70);
		
		Assertions.assertThat(resultador).isEqualTo("Pode votar");
	}
}
