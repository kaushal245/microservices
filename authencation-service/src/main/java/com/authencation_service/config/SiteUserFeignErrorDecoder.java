package com.authencation_service.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;


import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;

public class SiteUserFeignErrorDecoder implements ErrorDecoder {

	  private final ErrorDecoder defaultDecoder = new Default();
	    private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public Exception decode(String methodKey, Response response) {
		 try {
	            if (response.body() != null) {
	                String body = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
	                Map<String, Object> errorMap = objectMapper.readValue(body, Map.class);
	                String message = errorMap.getOrDefault("message", "Something went wrong").toString();

	                if (response.status() == 400) {
	                    return new BadRequestException(message);
	                }
	                if (response.status() == 404) {
	                    return new NotFoundException(message);
	                }
	                return new RuntimeException(message);
	            }
	        } catch (IOException e) {
	            // fall through to default
	        }
	        return defaultDecoder.decode(methodKey, response);
	}

}
