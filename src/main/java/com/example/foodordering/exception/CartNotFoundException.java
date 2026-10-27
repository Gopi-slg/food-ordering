package com.example.foodordering.exception;

public class CartNotFoundException extends RuntimeException {

	public CartNotFoundException(String message) {
		super(message);
	}
}
