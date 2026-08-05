package com.blog_service.blog_service.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<?> handleBadRequest(BadRequestException ex) {
		HashMap<String, Object> map = new HashMap<>();
		System.err.println("Exception: " + ex.getMessage());
		 ex.printStackTrace();
		map.put("success", false);
		map.put("message", ex.getMessage());
		return ResponseEntity.badRequest().body(map);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
		HashMap<String, Object> map = new HashMap<>();
		System.err.println("Exception: " + ex.getMessage());
		 ex.printStackTrace();
		map.put("success", false);
		map.put("message", ex.getMessage());
		return ResponseEntity.badRequest().body(map);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleException(Exception ex) {
		HashMap<String, Object> map = new HashMap<>();
		System.err.println("Exception: " + ex.getMessage());
		 ex.printStackTrace();
		map.put("success", false);
		map.put("message", ex.getMessage());
		map.put("message", "Something went wrong!!!!" + ex.getMessage());
		return ResponseEntity.internalServerError().body(map);
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<?> handleValidationException(ValidationException ex) {
		Map<String, Object> response = new HashMap<>();
		System.err.println("Exception: " + ex.getMessage());
		 ex.printStackTrace();
		response.put("success", false);
		response.put("message", "Validation failed");
		response.put("errors", ex.getErrors());
		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleNotFound(NotFoundException ex) {
	    Map<String, Object> body = new HashMap<>();
	    body.put("success", false);
	    body.put("message", ex.getMessage());
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
	}
	
	   @ExceptionHandler(AuthServiceUnavailableException.class)
	    public ResponseEntity<Map<String, Object>> handleAuthServiceDown(AuthServiceUnavailableException ex) {
	        Map<String, Object> body = new HashMap<>();
	        body.put("success", false);
	        body.put("message", "Authentication service is temporarily unavailable. Please try again later.");
	        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body); // 503
	    }
	   
	    @ExceptionHandler(feign.RetryableException.class)
	    public ResponseEntity<Map<String, Object>> handleFeignRetryable(feign.RetryableException ex) {
	        Map<String, Object> body = new HashMap<>();
	        body.put("success", false);
	        body.put("message", "Authentication service is currently unavailable. Please try again later.");
	        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body); // 503
	    }
	    
	    @ExceptionHandler(feign.FeignException.class)
	    public ResponseEntity<Map<String, Object>> handleFeignException(feign.FeignException ex) {
	        Map<String, Object> body = new HashMap<>();
	        body.put("success", false);
	        body.put("message", "Auth service error: " + ex.status());
	        return ResponseEntity.status(ex.status()).body(body);
	    }
	    
	    @ExceptionHandler(IllegalStateException.class)
	    public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException ex) {
	        Map<String, Object> body = new HashMap<>();
	        body.put("success", false);
	        if (ex.getMessage() != null && ex.getMessage().contains("Load balancer does not contain an instance")) {
	            body.put("message", "Authentication service is currently unavailable. Please try again later.");
	            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
	        }
	        body.put("message", "Something went wrong: " + ex.getMessage());
	        return ResponseEntity.internalServerError().body(body);
	    }
	    
	  

}
