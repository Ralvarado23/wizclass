package com.wizclass.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * This interface creates an annotation to validate Telefono variable.
 * @author Raul Alvarado
 *
 */
@Constraint(validatedBy = TelefonoValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Telefono {
    String message() default "El teléfono introducido no es válido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}