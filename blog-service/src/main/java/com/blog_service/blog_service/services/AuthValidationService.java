
package com.blog_service.blog_service.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog_service.blog_service.client.AuthServiceFeingClient;
import com.blog_service.blog_service.config.AuthServiceUnavailableException;
import com.blog_service.blog_service.dto.TokenValidationRequest;


@Service
public class AuthValidationService {
	 @Autowired
	    private AuthServiceFeingClient authFeignClient;

	  
	    public Map<String, Object> validateToken(TokenValidationRequest request) {
	        return authFeignClient.validateToken(request);
	    }

	    
}
