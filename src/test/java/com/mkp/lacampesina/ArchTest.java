package com.mkp.lacampesina;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.mkp.lacampesina");

        noClasses()
            .that()
            .resideInAnyPackage("com.mkp.lacampesina.service..")
            .or()
            .resideInAnyPackage("com.mkp.lacampesina.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.mkp.lacampesina.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
