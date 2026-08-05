package com.blog_service.blog_service.config;

import java.util.Map;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
	private final Map<String, String> errors;

	public ValidationException(Map<String, String> errors) {
		super();
		this.errors = errors;
	}

}
