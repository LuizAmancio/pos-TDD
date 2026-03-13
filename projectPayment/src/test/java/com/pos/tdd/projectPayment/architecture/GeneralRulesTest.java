package com.pos.tdd.projectPayment.architecture;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Repository;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.GeneralCodingRules;

import jakarta.persistence.Entity;


class GeneralRulesTest {
	
	private final static String CAMINHO_PACKAGE = "com.pos.tdd.projectPayment";
	
	private JavaClasses javaClasses = new ClassFileImporter()
			 .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS) // para desconsiderar os pacotes de teste
			.importPackages(CAMINHO_PACKAGE);
	
	
	@Test
	@DisplayName("Controller should not access the repository directly")
	void controllerTest() {
		
		ArchRule rule = ArchRuleDefinition.noClasses()
				.that().resideInAPackage("..controller..")
				.should().accessClassesThat().resideInAPackage("..repository..");
		
		rule.check(javaClasses);
	}
	
	
	@Test
	@DisplayName("Just interfaces are permit in repositoy's package")
	void justInterfaceInRepositoryPackageTest() {
		ArchRule rule = ArchRuleDefinition.classes()
				.that().resideInAPackage("..repository..")
				.should().beInterfaces();
		
		rule.check(javaClasses);
	}
	
	@Test
	@DisplayName("Interfaces with annottation @repository should be reside in repository package and his name ending with Repository")
	void justInterfaceinRepositoryPackageTest() {
		ArchRule rule = ArchRuleDefinition.classes()
				.that().areAnnotatedWith(Repository.class)
				.should().resideInAPackage("..repository..")
				.andShould().beInterfaces()
				.andShould().haveSimpleNameEndingWith("Repository");
		
		rule.check(javaClasses);
	}
	
	@Test
	@DisplayName("Classes with annottation @entity should be reside in model package")
	void entityTest() {
		ArchRule rule = ArchRuleDefinition.classes()
				.that().areAnnotatedWith(Entity.class)
				.should().resideInAPackage("..model..");
		
		rule.check(javaClasses);
	}
	
	@Test
	@DisplayName("Should not use generic exeptions")
	void shouldNotUseGenericExceptions() {
		ArchRule rule = GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;
		rule.check(javaClasses);
	}
	
	@Test
	@DisplayName("No classes should access @Deprecated members or should depend on @Deprecated classes")
	void shouldNotUseDeprecatedClasses() {
		ArchRule rule = GeneralCodingRules.DEPRECATED_API_SHOULD_NOT_BE_USED;
		rule.check(javaClasses);
	}

}
