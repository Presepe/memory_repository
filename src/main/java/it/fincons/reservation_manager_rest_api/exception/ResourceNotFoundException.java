package it.fincons.reservation_manager_rest_api.exception;

public class ResourceNotFoundException extends Exception {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
