package com.authencation_service.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authencation_service.dto.MFStackOtpDto;
import com.authencation_service.helpers.Helpers;



@RestController
@RequestMapping("/api/auth/common")
public class CommonController {
	
	@Autowired
	private Helpers commonMethod;
	@PostMapping("/encrypted_payload")
	public ResponseEntity<?> encryptMethod(@RequestBody MFStackOtpDto request) throws Exception {
		String isdCode = commonMethod.cipher(request.getIsdcode());
		String phoneNo = commonMethod.cipher((request.getPhone_no()));
		String otp = commonMethod.cipher((request.getOtp()));
		String emailId = commonMethod.cipher((request.getEmail_id()));
		String siteUserId = commonMethod.cipher(String.valueOf(request.getSite_user_id()));
		String name = commonMethod.cipher(request.getName());
		String headerPhoneNo = commonMethod.cipher(request.getHeaderPhoneNo());
		String headerEmailId = commonMethod.cipher(request.getHeaderEmailId());
		String tokenIdentifier = commonMethod.cipher(request.getIdentifier());
		Map<String, Object> response = new HashMap<>();
		response.put("isdCode", isdCode);
		response.put("phoneNo", phoneNo);
		response.put("otp", otp);
		response.put("emailId", emailId);
		response.put("siteUserId", siteUserId);
		response.put("name", name);
		response.put("headerPhoneNo", headerPhoneNo);
		response.put("headerEmailId", headerEmailId);
		response.put("Identifier", tokenIdentifier);
		return ResponseEntity.ok(response);
	}
	
	

	public Map<String, String> decrypted(MFStackOtpDto request) throws Exception {
//		System.err.println("isdCode = " + request.getIsdcode());
//		System.err.println("phoneNo = " + request.getPhone_no());
//		System.err.println("otp = " + request.getOtp());
//		System.err.println("emailId = " + request.getEmail_id());
//		System.err.println("siteUserId = " + request.getSite_user_id());
//		System.err.println("name = " + request.getName());
		Map<String, String> response = new HashMap<>();
		response.put("isdCode", request.getIsdcode() != null ? commonMethod.decipher(request.getIsdcode()) : null);
		response.put("phoneNo", request.getPhone_no() != null ? commonMethod.decipher(request.getPhone_no()) : null);
		response.put("otp", request.getOtp() != null ? commonMethod.decipher(request.getOtp()) : null);
		response.put("emailId", request.getEmail_id() != null ? commonMethod.decipher(request.getEmail_id()) : null);
		response.put("siteUserId",request.getSite_user_id() != null ? commonMethod.decipher(request.getSite_user_id()) : null);
		response.put("name", request.getName() != null ? commonMethod.decipher(request.getName()) : null);
		response.put("headerPhoneNo",request.getHeaderPhoneNo() != null ? commonMethod.decipher(request.getHeaderPhoneNo()) : null);
		response.put("headerEmailId",request.getHeaderEmailId() != null ? commonMethod.decipher(request.getHeaderEmailId()) : null);
		
		response.put("Identifier",request.getIdentifier()!=null ?  commonMethod.decipher(request.getIdentifier()) : null );
		return response;
	}

}
