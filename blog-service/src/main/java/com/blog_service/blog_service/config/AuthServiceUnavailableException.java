package com.blog_service.blog_service.config;

public class AuthServiceUnavailableException extends RuntimeException {
    public AuthServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
