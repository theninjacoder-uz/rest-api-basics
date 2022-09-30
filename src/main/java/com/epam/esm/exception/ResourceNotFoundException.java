package com.epam.esm.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(long id) {
        super("Data not found with the id: " + id);
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
