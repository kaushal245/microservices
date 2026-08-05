package com.authencation_service.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authencation_service.dto.TokenValidationRequest;
import com.authencation_service.helpers.Helpers;
import com.authencation_service.reposatory.LoginTokenRepo;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private Helpers helper;
	
	@Autowired
	private LoginTokenRepo loginTokenRepo;
	
	
	@PostMapping("/validate-token")
	public ResponseEntity<Map<String, Object>> validateToken(
	        @RequestBody TokenValidationRequest request) throws Exception {
		System.err.println("Email Id : " + request.getEmailId());
		System.err.println("Blog Id : " + request.getBlogId());
		System.err.println("Site User Id : " + request.getSiteUserId());
		System.err.println("Token : " + request.getToken());
		
		
		String emailId = helper.decipher(request.getEmailId());
		String blogId = helper.decipher(request.getBlogId());
		String siteUserId = helper.decipher(request.getSiteUserId());
		
		request.setEmailId(emailId);
		request.setBlogId(blogId);
		request.setSiteUserId(siteUserId);
		
		helper.validateRequest(
	            Map.of("Token", request.getToken(), "Email Id", request.getEmailId()));

	    boolean isValid = helper.validateLoginToken(
	            request.getEmailId().trim(),
	            request.getToken().trim());

	    System.err.println(" Is Valid Token "+ isValid +" Email Id " +  request.getEmailId().trim() +"======== "+blogId+" ===> "+siteUserId);
	    
	    if (!isValid) {
	        return ResponseEntity.badRequest().body(Map.of(
	                "success", false,
	                "message", "Token is invalid or expired"));
	    }

	    loginTokenRepo.updateTokenStatus(
	            request.getEmailId(),
	            request.getToken());
	    Map<String, Object> data = new HashMap<>();

	    data.put("emailId", emailId);
	    data.put("blogId", blogId);
	    data.put("siteUserId", siteUserId);

	    return ResponseEntity.ok(Map.of(
	            "success", true,
	            "message", "Token validated successfully", "data", data));
	}
}
