package it.fincons.reservation_manager_rest_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalTime;

import it.fincons.reservation_manager_rest_api.dto.CreateBookingRequest;

//contiene la logica del controllo
public class ValidTimeRangeValidator implements ConstraintValidator<ValidTimeRange, CreateBookingRequest> {


    @Override
    public boolean isValid(CreateBookingRequest booking, ConstraintValidatorContext context) {

        if (booking == null) {
            return true;
        }

        LocalTime start = booking.getStartTime();
        LocalTime end = booking.getEndTime();

        if (start == null || end == null) {
            return true;
        }

        return end.isAfter(start);
    }
}
