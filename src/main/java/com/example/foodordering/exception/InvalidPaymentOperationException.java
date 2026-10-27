package com.example.foodordering.exception;

public class InvalidPaymentOperationException extends RuntimeException {
    public InvalidPaymentOperationException(String message) { super(message); }
}
