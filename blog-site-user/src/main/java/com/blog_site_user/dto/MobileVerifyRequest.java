package com.blog_site_user.dto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

@Data
public class MobileVerifyRequest {
	private String identifier;
	private String isdcode;
	private String name;
	private String otp;
	private String phone_no;
	private String headerPhoneNo;
	private String headerEmailId;
	private String email_id;
	private String token;
	private String site_user_id;
	private HttpServletRequest request;
	private String fcmToken;
	private Integer UserId;
}
