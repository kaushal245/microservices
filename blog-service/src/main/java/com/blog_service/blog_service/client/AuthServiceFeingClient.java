package com.blog_service.blog_service.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.blog_service.blog_service.config.FeignConfig;
import com.blog_service.blog_service.config.SiteUserFeignConfig;
import com.blog_service.blog_service.dto.BlogSiteUserDto;
import com.blog_service.blog_service.dto.TokenValidationRequest;

@FeignClient(name = "authencation-service", configuration = FeignConfig.class)
public interface AuthServiceFeingClient {
	@PostMapping("/api/auth/validate-token")
	Map<String, Object> validateToken(@RequestBody TokenValidationRequest request);
}
