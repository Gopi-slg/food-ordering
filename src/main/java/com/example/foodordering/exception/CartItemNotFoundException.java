package com.example.foodordering.exception;

public class CartItemNotFoundException extends RuntimeException {

	public CartItemNotFoundException(String message) {
		super(message);
	}
}
