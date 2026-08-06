package com.authencation_service.client;

import java.util.HashMap;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.authencation_service.dto.MFStackOtpDto;
import com.authencation_service.dto.siteUserResponse;



@FeignClient(name = "blog-site-user")
public interface SiteUserFeignClient {
	@PostMapping("/api/site-user/verify-mobile")
	HashMap<String, Object> verifyMobile(@RequestBody MFStackOtpDto request);
	
	
	 @PostMapping("/api/site-user/check-email")
	 HashMap<String, Object> checkEmail(@RequestBody MFStackOtpDto request);

		@PostMapping("/api/site-user/verify-email")
		HashMap<String, Object> verifyEmail(@RequestBody MFStackOtpDto request);
	    @PostMapping("/api/site-user/update-name")
	    HashMap<String, Object> updateName(@RequestBody MFStackOtpDto request);


	    @GetMapping("/api/site-user/{userId}")
	    siteUserResponse getUser(@PathVariable Integer userId);
	    
	    @PostMapping("/api/site-user/email-verify-with-google")
	    public HashMap<String,Object> updateEmailWithGoogle(@RequestBody MFStackOtpDto request);
	    
		@PostMapping("/api/site-user/login/validate-login-user")
		HashMap<String, Object> validateLoginUser(@RequestBody MFStackOtpDto request);
		
		@PostMapping("/api/site-user/login/mobile/send-otp")
	    HashMap<String, Object> sendMobileOtp(@RequestBody MFStackOtpDto request);

	    @PostMapping("/api/site-user/login/mobile/verify")
	    HashMap<String, Object> verifyMobileOtp(@RequestBody MFStackOtpDto request);

	    @PostMapping("/api/site-user/login/email/send-otp")
	    HashMap<String, Object> sendEmailOtp(@RequestBody MFStackOtpDto request);

	    @PostMapping("/api/site-user/login/email/verify")
	    HashMap<String, Object> verifyEmailOtp(@RequestBody MFStackOtpDto request);

	    @PostMapping("/api/site-user/login/google")
	    HashMap<String, Object> googleLogin(@RequestBody MFStackOtpDto request);

	    @PostMapping("/api/site-user/login/validate")
	    HashMap<String, Object> validateLogin(@RequestBody MFStackOtpDto request);
}
