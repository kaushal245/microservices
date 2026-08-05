package com.blog_service.blog_service.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog_service.blog_service.client.AuthServiceFeingClient;
import com.blog_service.blog_service.config.SiteUserFeignConfig;
import com.blog_service.blog_service.dto.BlogDTO;
import com.blog_service.blog_service.dto.BlogSiteUserDto;
import com.blog_service.blog_service.dto.TokenValidationRequest;
import com.blog_service.blog_service.services.AuthValidationService;
import com.blog_service.blog_service.services.BlogSiteUserService;

@RestController
@RequestMapping("/api/blog")
public class BlogSiteUser {

	@Autowired
	private BlogSiteUserService service;
	
	  @Autowired
	    private AuthServiceFeingClient authFeignClient;
	  
	  @Autowired
	  private AuthValidationService authValidationService; 
	
	@PostMapping("/save")
	public ResponseEntity<?> saveBlogSiteUser(@RequestBody TokenValidationRequest request,
			@RequestHeader(value = "token", required = true) String token) throws Exception {
		
		request.setToken(token);
		System.err.println(" Request "+request.toString());
		
		String apiToken = Optional.ofNullable(token).map(String::trim).filter(s -> !s.isBlank())
				.orElseThrow(() -> new BadRequestException("Token is required"));
		
		Map<String, Object> authResponse = authFeignClient.validateToken(request);
		Map<String, Object> data = (Map<String, Object>) authResponse.get("data");
		
		System.err.println("Data +++++++++ "+ data);
		
		String emailId = (String) data.get("emailId");
		System.err.println("Data +++++++++ "+ emailId);
		String blogSiteUserId =  (String) data.get("blogId");
		System.err.println("Data +++++++++ "+ blogSiteUserId);
		String siteUserId = (String) data.get("siteUserId");
		System.err.println("Data +++++++++ "+ siteUserId);
		request.setBlogId(blogSiteUserId);
		request.setSiteUserId(siteUserId);
		
		Map<String, Object> response = service.saveBlogSiteUser(request);
		
		Boolean status = (Boolean) response.get("success");
		if (Boolean.TRUE.equals(status)) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.badRequest().body(response);
	}
	
//	@PostMapping("/save")
//	public ResponseEntity<?> saveBlogSiteUser(@RequestBody BlogSiteUserDto request) throws Exception {
//		Map<String, Object> response = service.saveBlogSiteUser(request);
//		Boolean status = (Boolean) response.get("success");
//		if (Boolean.TRUE.equals(status)) {
//			return ResponseEntity.ok(response);
//		}
//		return ResponseEntity.badRequest().body(response);
//	}

	@GetMapping("/details/{blogSiteUserId}")
	public ResponseEntity<?> getBlogSiteUserId(@PathVariable String blogSiteUserId,@RequestBody TokenValidationRequest request,
			@RequestHeader(value = "token", required = true) String token) throws Exception {
		request.setToken(token);
		System.err.println(" Request "+request.toString());
		
		String apiToken = Optional.ofNullable(token).map(String::trim).filter(s -> !s.isBlank())
				.orElseThrow(() -> new BadRequestException("Token is required"));
		
		Map<String, Object> authResponse = authFeignClient.validateToken(request);
		Map<String, Object> data = (Map<String, Object>) authResponse.get("data");
		
		String emailId = (String) data.get("emailId");
		String blogSiteUserId1 =  (String) data.get("blogSiteUserId");
		String siteUserId = (String) data.get("siteUserId");
	
		request.setBlogId(blogSiteUserId1);
		request.setSiteUserId(siteUserId);
		List<BlogDTO> blog = service.getBlogSiteUser(Integer.valueOf(blogSiteUserId1));
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("data", blog);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/delete")
	public ResponseEntity<?> deleteBlogSiteUser(@RequestBody BlogSiteUserDto request) throws Exception {

		
		Map<String, Object> response = service.softDeleteBlogs(request);
		Boolean status = (Boolean) response.get("success");
		if (Boolean.TRUE.equals(status)) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.badRequest().body(response);
	}
}
