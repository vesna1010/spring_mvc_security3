package com.vesna1010.onlineshop.exception;

public class CategoryCanNotDeleteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CategoryCanNotDeleteException(String message) {
		super(message);
	}
}
