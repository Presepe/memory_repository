package it.fincons.reservation_manager_rest_api.exception;

public class InvalidBookingException extends Exception{
    public InvalidBookingException(String message) {
        super(message);
    }
}
