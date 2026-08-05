package com.blog_service.blog_service.dto;

import lombok.Data;

@Data
public class TokenValidationRequest {
	 private String emailId;
	    private String token;
	    private String blogId;
	    private String siteUserId;
}
