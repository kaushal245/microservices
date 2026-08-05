package com.blog_site_user.config;

public class BadRequestException extends RuntimeException {
	 private static final long serialVersionUID = 1L;

	    public BadRequestException(String message) {
	        super(message);
	    }
}
