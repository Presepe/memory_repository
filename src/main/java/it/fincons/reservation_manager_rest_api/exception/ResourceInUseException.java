package it.fincons.reservation_manager_rest_api.exception;

public class ResourceInUseException extends Exception{
    public ResourceInUseException(String message) {
        super(message);
    }
}
