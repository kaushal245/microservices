package com.authencation_service.dto;

import java.sql.Timestamp;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class MFStackOtpDto {
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
	
}
