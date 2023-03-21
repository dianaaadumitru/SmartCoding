package com.example.backend.exceptions;

public class CrudOperationException extends RuntimeException {
    public CrudOperationException(String message) {
        super(message);
    }
}
