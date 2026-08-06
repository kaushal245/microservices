package com.example.apigateway.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Component
@Order(-1)
public class GlobalExceptionHandler implements org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

		exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
		Map<String, Object> response = new HashMap<>();
		response.put("success", false);
		if (ex.getMessage() != null && ex.getMessage().contains("Connection refused")) {
			response.put("message", "Service is currently unavailable");
			response.put("error", "DOWNSTREAM_SERVICE_ERROR");
		} else {
			response.put("message", "Something went wrong");
			response.put("error", ex.getMessage());
		}
		byte[] bytes;
		try {
			bytes = objectMapper.writeValueAsBytes(response);
		} catch (Exception e) {
			bytes = "{\"success\":false,\"message\":\"Internal Server Error\"}".getBytes();
		}
		return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
	}
}