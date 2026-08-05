package com.blog_service.blog_service.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.blog_service.blog_service.config.SiteUserFeignConfig;

@FeignClient(name = "site-user-service", configuration = SiteUserFeignConfig.class)
public interface SiteUserFeingClient {

	@GetMapping("/api/site-user/{userId}")
	Map<String, Object> getUserById(@PathVariable("userId") Integer userId);
}
