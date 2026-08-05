package com.blog_site_user.controllers;

import java.util.HashMap;
import java.util.List;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.blog_site_user.dto.MobileVerifyRequest;
import com.blog_site_user.dto.SiteUserResponse;
import com.blog_site_user.services.SiteUserService;

@RestController
@RequestMapping("/api/site-user")
public class SiteUserController {

	   @Autowired
	    private SiteUserService siteUserService;

	    @PostMapping("/verify-mobile")
	    public HashMap<String, Object> verifyMobile(@RequestBody MobileVerifyRequest request) {
	    	System.err.println(request.getPhone_no());
	        return siteUserService.verifyMobile(request);
	    }
	    
	    @PostMapping("/check-email")
	    public HashMap<String,Object> checkEmail(
	            @RequestBody MobileVerifyRequest request)  {

	        return siteUserService.checkEmail(request);
	    }


	    @PostMapping("/verify-email")
	    public HashMap<String,Object> verifyEmail(
	            @RequestBody MobileVerifyRequest request)  {
	    	
	    	System.err.println(request.getPhone_no());
	        return siteUserService.verifyEmail(request);
	    }


	    @PostMapping("/update-name")
	    public HashMap<String,Object> updateName(
	            @RequestBody MobileVerifyRequest request)  {

	        return siteUserService.updateName(request);
	    }
	    
	    @PostMapping("/email-verify-with-google")
	    public HashMap<String,Object> updateEmailWithGoogle(
	            @RequestBody MobileVerifyRequest request)  {

	        return siteUserService.googleEmailVerify(request);
	    }


	    @GetMapping("/{userId}")
	    public SiteUserResponse getUser(
	            @PathVariable Integer userId) {

	        return siteUserService.getUser(userId);
	    }
	    
		@PostMapping("/login/validate-login-user")
		public HashMap<String, Object> validateLoginUser(@RequestBody MobileVerifyRequest request)
				throws BadRequestException {

			return siteUserService.validateLoginUser(request);
		}
	    @PostMapping("/login/validate")
	    public HashMap<String,Object> validateLogin(@RequestBody MobileVerifyRequest request){	    
	    	siteUserService.validateLoginUser(request);
	        HashMap<String,Object> map=new HashMap<>();
	        map.put("success",true);

	        return map;
	    }
	    
	    @PostMapping("/login/mobile/verify")
	    public HashMap<String,Object> verifyMobileOtp(@RequestBody MobileVerifyRequest request){
	        return siteUserService.mobileLogin(request);
	    }
	    
	    @PostMapping("/login/email/verify")
	    public HashMap<String,Object> verifyEmailOtp(@RequestBody MobileVerifyRequest request){
	        return siteUserService.emailLogin(request);
	    }
	    
		@PostMapping("/login/google")
		public HashMap<String, Object> googleLogin(@RequestBody MobileVerifyRequest request) {
			return siteUserService.googleLogin(request);
		}
}
