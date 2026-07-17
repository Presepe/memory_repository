package it.fincons.reservation_manager_rest_api.exception;

public class BookingConflictException extends Exception{
    public BookingConflictException(String message) {
        super(message);
    }
}
