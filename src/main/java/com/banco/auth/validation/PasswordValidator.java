package com.banco.auth.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {

        if (password == null) {
            return false;
        }

        // Validación básica con regex
        if (!password.matches(PASSWORD_PATTERN)) {
            return false;
        }

        // Validación personalizada: no contener el nombre de usuario
        // Esto lo haremos más adelante cuando tengamos el DTO

        return true;
    }
}
