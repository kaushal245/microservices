package com.authencation_service.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.coyote.BadRequestException;
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

	

}
