package com.raise_ticket_service.client;

import java.util.HashMap;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "blog-site-user")
public interface SIteUserLoginClient {

	@GetMapping("/api/site-user/support/{siteUserId}")
	public HashMap<String, Object> getUserContactBySiteUserId(@PathVariable Integer siteUserId);
}
