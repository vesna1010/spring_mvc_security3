package com.vesna1010.onlineshop.exception;

public class ProductCanNotOrderException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ProductCanNotOrderException(String message) {
		super(message);
	}
	
}
