package com.banco.auth.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        if (factory != null) {
            factory.close();
        }
    }

    private RegisterRequest baseValidRequest() {
        return RegisterRequest.builder()
                .username("JuanPerez")
                .password("Str0ng!Passw0rd")
                .email("juan.perez@example.com")
                .firstName("Juan")
                .lastName("Perez")
                .role("USER")
                .build();
    }

    @Test
    @DisplayName("Debe fallar cuando la contraseña contiene el username (case-insensitive)")
    void shouldFailWhenPasswordContainsUsername() {
        RegisterRequest request = baseValidRequest();
        // Incluir el username (con distintas mayúsculas/minúsculas) dentro del password
        request.setPassword("xxjuanperezXX1!");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty(), "Se esperaba una violación de validación");
        boolean hasContainUsernameMessage = violations.stream()
                .anyMatch(v -> "La contraseña no puede contener el nombre de usuario".equals(v.getMessage()));
        assertTrue(hasContainUsernameMessage, "Debe incluir el mensaje por contener el username en la contraseña");
    }

    @Test
    @DisplayName("Debe fallar cuando la contraseña es demasiado corta (mínimo 8)")
    void shouldFailWhenPasswordTooShort() {
        RegisterRequest request = baseValidRequest();
        // 6 caracteres, aunque cumpla con clases de caracteres, debe fallar por longitud mínima 8
        request.setPassword("Aa1!aa");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty(), "Se esperaba una violación de validación por contraseña corta");
        boolean hasPasswordPolicyMessage = violations.stream()
                .anyMatch(v -> "La contraseña no cumple con los requisitos de seguridad".equals(v.getMessage()));
        assertTrue(hasPasswordPolicyMessage, "Debe fallar con el mensaje del validador de contraseña");
    }

    @Test
    @DisplayName("Debe pasar cuando la contraseña es fuerte y no contiene el username")
    void shouldPassWhenPasswordStrongAndDoesNotContainUsername() {
        RegisterRequest request = baseValidRequest();
        // Asegurar que no contiene el username
        request.setPassword("Sup3r!S3gura");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty(), () -> "No se esperaban violaciones, pero se encontraron: " + violations);
    }
}
