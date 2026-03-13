package com.pos.tdd.projectPayment.dataDriven;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ValueSourceTest {

	@ParameterizedTest(name = "Pode votar com a idade {0}")
	@ValueSource(ints = {18,20,30,40,50,60,70})
	void valueSourcetest(int idade) {
		Assertions.assertThat(podeVotar(idade)).isTrue();
	}
	
	public boolean podeVotar(int idade) {
		
		if(idade >= 18 && idade <= 70) {
			return true;
		}
		
		return false;
	}
	
}
