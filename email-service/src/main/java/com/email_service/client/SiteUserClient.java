package com.email_service.client;

import java.util.HashMap;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "blog-site-user")
public interface SiteUserClient {
	  @GetMapping(
	            "/api/site-user/contact/{siteUserId}")
	    HashMap<String, Object>
	    getUserContactBySiteUserId(
	            @PathVariable Integer siteUserId);
}
