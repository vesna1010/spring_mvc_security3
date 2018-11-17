package com.vesna1010.onlineshop.exception;


public class ProductCanNotDeleteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ProductCanNotDeleteException(String message) {
		super(message);
	}
	
}