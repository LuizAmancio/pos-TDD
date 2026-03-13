package com.pos.tdd.projectPayment.dataDriven;

import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import com.pos.tdd.projectPayment.dataDriven.MethodSourceTest.Pessoa;

import net.datafaker.Faker;

public class PessoaArgumentSourceData implements ArgumentsProvider {
	
	// Uso do DataFaker
	private final Faker faker = new Faker(Locale.of("pt-BR"));
	
	@Override
	public Stream<? extends Arguments> provideArguments (ExtensionContext context){
		
		System.out.println(faker.name().fullName());
		System.out.println(faker.address().fullAddress());
		System.out.println(faker.cpf().valid());
		
		return Stream.of(
				Arguments.arguments(new Pessoa(faker.name().fullName(), 18), "Pode votar"),
				Arguments.arguments(new Pessoa(faker.name().fullName(), 28), "Pode votar")
				);
	}

}
