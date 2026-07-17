package it.fincons.reservation_manager_rest_api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = ValidTimeRangeValidator.class)

public @interface ValidTimeRange {

    String message() default "L'orario di fine deve essere successivo all'orario di inizio";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

