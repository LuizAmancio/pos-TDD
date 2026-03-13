package com.pos.tdd.projectPayment.dataDriven;

import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MethodSourceTest {
	
	@ParameterizedTest
	@MethodSource("fonteDados")
	void podeVotarTest(int idade, String resultador) {
		Assertions.assertThat(idade)
			.isGreaterThanOrEqualTo(18)
			.isLessThanOrEqualTo(70);
		
		Assertions.assertThat(resultador).isEqualTo("Pode votar");
	}
	
	@ParameterizedTest
	@MethodSource("fonteDadosPessoa")
	void podeVotarObjectTest(Pessoa pessoa, String resultador) {
		Assertions.assertThat(pessoa.getIdade())
		.isGreaterThanOrEqualTo(18)
		.isLessThanOrEqualTo(70);
		
		Assertions.assertThat(resultador).isEqualTo("Pode votar");
	}

	static Stream<Arguments> fonteDados(){
		return Stream.of(
					Arguments.arguments(18, "Pode votar"),
					Arguments.arguments(58, "Pode votar")
				);
	}
	
	static Stream<Arguments> fonteDadosPessoa(){
		return Stream.of(
				Arguments.arguments(new Pessoa("João", 18), "Pode votar"),
				Arguments.arguments(new Pessoa("Maria", 28), "Pode votar")
				);
	}
	
	static class Pessoa {
		private String nome;
		private int idade;
		
		public Pessoa(String nome, int idade) {
			super();
			this.nome = nome;
			this.idade = idade;
		}
		public String getNome() {
			return nome;
		}
		public void setNome(String nome) {
			this.nome = nome;
		}
		public int getIdade() {
			return idade;
		}
		public void setIdade(int idade) {
			this.idade = idade;
		}
	}
	
}
