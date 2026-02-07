package com.banco.auth.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserRegistrationValidator.class)
public @interface ValidUserRegistration {

    String message() default "Los datos de registro no son válidos";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
