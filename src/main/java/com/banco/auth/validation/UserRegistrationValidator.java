package com.banco.auth.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserRegistrationValidator implements ConstraintValidator<ValidUserRegistration, Object> {

    @Override
    public boolean isValid(Object dto, ConstraintValidatorContext context) {
        try {
            // Usamos reflection para obtener los valores del DTO
            String username = (String) dto.getClass().getMethod("getUsername").invoke(dto);
            String password = (String) dto.getClass().getMethod("getPassword").invoke(dto);

            // Validar que el password no contenga el username (case-insensitive)
            if (username != null && password != null) {
                String usernameLower = username.toLowerCase();
                String passwordLower = password.toLowerCase();

                if (passwordLower.contains(usernameLower)) {
                    // Personalizar el mensaje de error
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(
                            "La contraseña no puede contener el nombre de usuario"
                    ).addConstraintViolation();
                    return false;
                }
            }

            return true;

        } catch (Exception e) {
            // Si hay error al acceder a los campos, rechazamos la validación
            return false;
        }
    }
}
