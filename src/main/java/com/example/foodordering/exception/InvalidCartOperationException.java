package com.example.foodordering.exception;

public class InvalidCartOperationException extends RuntimeException {

	public InvalidCartOperationException(String message) {
		super(message);
	}
}
