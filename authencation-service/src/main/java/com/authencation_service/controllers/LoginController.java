package com.authencation_service.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authencation_service.client.SiteUserFeignClient;
import com.authencation_service.config.JwtUtil;
import com.authencation_service.dto.MFStackOtpDto;
import com.authencation_service.helpers.Helpers;
import com.authencation_service.reposatory.LoginTokenRepo;
import com.authencation_service.reposatory.UserOtpRepository;
import com.authencation_service.services.LoginService;




@RestController
@RequestMapping("/api/auth/login")
public class LoginController {
	

	@Autowired
	private UserOtpRepository otpRepo;

	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private Helpers commonFunction;

	@Autowired
	private LoginTokenRepo loginTokenRepo;

	@Autowired
	private LoginService loginService;

	@Autowired
	private CommonController commonMethod;
	
	
	@Autowired
	private SiteUserFeignClient client;
	
	@PostMapping("/generate_token")
	public ResponseEntity<?> generateToken(@RequestBody MFStackOtpDto request,
			@RequestHeader(value = "identifier", required = true) String identifier)
			throws RuntimeException, BadRequestException , Exception {
		String tokenIdentifier = identifier= commonMethod.decrypted(request).get("Identifier");
	//	System.err.println("Phone no "+identifier);
		request.setIdentifier(tokenIdentifier);
		String finalIdentifier = Optional.ofNullable(identifier).map(String::trim).filter(s -> !s.isBlank())
				.orElseThrow(() -> new RuntimeException("Identifier is required"));

		String userIdentifier = request.getIdentifier();
		if (userIdentifier.contains("@")) {

			if (!commonFunction.isEmail(userIdentifier)) {
				throw new BadRequestException("Only valid email id should be allowed to enter");
			}

		} else {

			// Mobile Validation
			if (!commonFunction.isNumeric(userIdentifier)) {
				throw new BadRequestException("Identifier must be a valid email or mobile number");
			}

			if (userIdentifier.length() != 10) {
				throw new BadRequestException("Mobile no must be 10 digit");
			}
		}

		Optional.of(finalIdentifier).filter(id -> id.equalsIgnoreCase(userIdentifier))
				.orElseThrow(() -> new BadRequestException("Something went wrong!"));

		Map<String, Object> data = loginService.generateAccessToken(request);
		return ResponseEntity.ok(Map.of("success", true, "message", "Token generate successfully", "data", data));
	}

	@PostMapping("/mobile_otp")
	public ResponseEntity<?> sendMobileOtp(@RequestBody MFStackOtpDto request,
			@RequestHeader(value = "token", required = true) String token,
			@RequestHeader(value = "phone_no", required = true) String phoneNo) throws Exception {
		
		
		
		request.setHeaderPhoneNo(phoneNo);
		String headerPhone = phoneNo = commonMethod.decrypted(request).get("headerPhoneNo");
		String bodyPhoneNo = commonMethod.decrypted(request).get("phoneNo");
		String bodyIsdcode = commonMethod.decrypted(request).get("isdCode");
		System.err.println(bodyIsdcode);
		request.setPhone_no(bodyPhoneNo);
		
		request.setIsdcode(bodyIsdcode);
		String apiToken = Optional.ofNullable(token).map(String::trim).filter(s -> !s.isBlank())
				.orElseThrow(() -> new BadRequestException("Token is required"));
		Optional.ofNullable(request.getPhone_no()).map(String::trim).filter(s -> !s.isBlank())
				.orElseThrow(() -> new BadRequestException("Phone number is required"));
		
		
		if (!commonFunction.isNumeric(phoneNo) || !commonFunction.isNumeric(request.getPhone_no())) {
			System.err.println("Mobile no "+ request.getPhone_no());
			throw new BadRequestException("Phone number must contain only numbers");
		}

		if (!request.getPhone_no().matches("\\d{10}")) {
			throw new BadRequestException("Invalid phone number");
		}

		commonFunction.validateRequest(Map.of("Token", token, "Phone number", phoneNo));

		commonFunction.validatePhoneNumber(phoneNo, request.getPhone_no());
		request.setToken(token);
		String mtoken = request.getToken();
		System.err.println(request.getPhone_no());
		boolean isValid = commonFunction.validateLoginToken(request.getPhone_no(), mtoken);
		System.err.println(isValid);
		
		Optional.of(isValid).filter(Boolean::booleanValue)
				.orElseThrow(() -> new RuntimeException("Token is invalid or expired"));
		
		loginTokenRepo.updateTokenStatus(phoneNo, mtoken);
		HashMap<String, Object> response;

	    client.validateLoginUser(request);
		response = loginService.SendMobileOtp(request); // set status 2 to the token //
		Boolean status = (Boolean) response.get("success");
		if (Boolean.TRUE.equals(status)) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.badRequest().body(response);
	}

	@PostMapping("/mobile_otp_verify")
	public ResponseEntity<?> mobileOtpVerify(@RequestBody MFStackOtpDto request,
			@RequestHeader(value = "token", required = true) String token,
			@RequestHeader(value = "phone_no", required = true) String phoneNo) throws BadRequestException,Exception {
		
		request.setHeaderPhoneNo(phoneNo);
		String headerPhone = phoneNo = commonMethod.decrypted(request).get("headerPhoneNo");
		String bodyPhoneNo = commonMethod.decrypted(request).get("phoneNo");
		String bodyIsdcode = commonMethod.decrypted(request).get("isdCode");
		String bodyPhoneOtp = commonMethod.decrypted(request).get("otp");
		request.setPhone_no(bodyPhoneNo);
		request.setIsdcode(bodyIsdcode);
		request.setOtp(bodyPhoneOtp);
		Optional.ofNullable(bodyPhoneNo).map(String::trim).filter(s -> !s.isBlank())
				.orElseThrow(() -> new BadRequestException("Phone number is required"));

		if (!commonFunction.isNumeric(phoneNo) || !commonFunction.isNumeric(request.getPhone_no())) {
			throw new BadRequestException("Phone number must contain only numbers");
		}

		if (!request.getPhone_no().matches("\\d{10}")) {
			throw new BadRequestException("Invalid phone no");
		}
		commonFunction.validateRequest(Map.of("Token", token, "Phone number", phoneNo));
		request.setToken(token);
		String mtoken = request.getToken();
		boolean isValid = commonFunction.validateLoginToken(request.getPhone_no(), mtoken);
		Optional.of(isValid).filter(Boolean::booleanValue)
				.orElseThrow(() -> new RuntimeException("Token is invalid or expired"));
		loginTokenRepo.updateTokenStatus(phoneNo, mtoken);
		HashMap<String, Object> response = loginService.mobileOtpVerify(request);
		Boolean status = (Boolean) response.get("success");
		Integer flag = (Integer) response.get("flag");
		if (Boolean.TRUE.equals(status) || Integer.valueOf(1).equals(flag)) {
			response.remove("flag");
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.badRequest().body(response);
	}

	@PostMapping("/email_otp")
	public ResponseEntity<?> sendEmailOtp(@RequestBody MFStackOtpDto request,
			@RequestHeader(value = "token", required = true) String token,
			@RequestHeader(value = "email_id", required = true) String emailId) throws Exception {
		
		
		request.setHeaderEmailId(emailId);
		String headerEmailId = emailId =commonMethod.decrypted(request).get("headerEmailId");
		String bodyEmailId = commonMethod.decrypted(request).get("emailId");
		
		request.setToken(token);
		request.setEmail_id(bodyEmailId.trim());
		
		
		
		commonFunction.validateRequest(Map.of("Token", token, "Email Id", emailId));
		request.setToken(token);
		String mtoken = request.getToken();
		boolean isValid = commonFunction.validateLoginToken(emailId, mtoken);
		Optional.of(isValid).filter(Boolean::booleanValue)
				.orElseThrow(() -> new RuntimeException("Token is invalid or expired"));
		loginTokenRepo.updateTokenStatus(emailId, mtoken);

		commonFunction.validateEmailLoginUser(request);
		client.validateLoginUser(request);
		HashMap<String, Object> response = loginService.SendEmailOtp(request);
		Boolean status = (Boolean) response.get("success");
		if (Boolean.TRUE.equals(status)) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.badRequest().body(response);

	}

	@PostMapping("/email_otp_verify")
	public ResponseEntity<?> emailOtpVerify(@RequestBody MFStackOtpDto request,
			@RequestHeader(value = "token", required = true) String token,
			@RequestHeader(value = "email_id", required = true) String emailId) throws BadRequestException,Exception {
		
		request.setHeaderEmailId(emailId);
		String headerEmailId = emailId =commonMethod.decrypted(request).get("headerEmailId");
		String bodyEmailId = commonMethod.decrypted(request).get("emailId");
		String bodyEmailOtp = commonMethod.decrypted(request).get("otp");
		request.setToken(token);
		request.setEmail_id(bodyEmailId.trim());
		request.setOtp(bodyEmailOtp);
		
		
		commonFunction.validateRequest(Map.of("Token", token, "Email Id", emailId));
		request.setToken(token);
		String mtoken = request.getToken();
		
		//System.err.println("Verify Emailid "+ emailId);
		boolean isValid = commonFunction.validateLoginToken(emailId, mtoken.trim());
		//System.err.println(" IsValid " + isValid);
		
		
		Optional.of(isValid).filter(Boolean::booleanValue)
				.orElseThrow(() -> new RuntimeException("Token is invalid or expired"));
		loginTokenRepo.updateTokenStatus(emailId, mtoken);
		commonFunction.validateEmailLoginUser(request);
		HashMap<String, Object> response = loginService.emailOtpVerify(request);
		Boolean status = (Boolean) response.get("success");
		Integer flag = (Integer) response.get("flag");
		if (Boolean.TRUE.equals(status) || Integer.valueOf(1).equals(flag)) {
			response.remove("flag");
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.badRequest().body(response);
	}

	@PostMapping("/login_with_google")
	public ResponseEntity<?> googleEmailUpdate(@RequestBody MFStackOtpDto request,
			@RequestHeader(value = "token", required = true) String token,
			@RequestHeader(value = "email_id", required = true) String emailId) throws BadRequestException,Exception {
		
		request.setHeaderEmailId(emailId);
		String headerEmailId = emailId =commonMethod.decrypted(request).get("headerEmailId");
		String bodyEmailId = commonMethod.decrypted(request).get("emailId");
//		String bodyEmailOtp = commonMethod.decrypted(request).get("otp");
		request.setToken(token);
		request.setEmail_id(bodyEmailId.trim());
//		request.setOtp(bodyEmailOtp);
		commonFunction.validateRequest(Map.of("Token", token, "Email Id", emailId));
		request.setToken(token);
		String mtoken = request.getToken();
		boolean isValid = commonFunction.validateLoginToken(emailId, mtoken);
		Optional.of(isValid).filter(Boolean::booleanValue)
				.orElseThrow(() -> new RuntimeException("Token is invalid or expired"));
		loginTokenRepo.updateTokenStatus(emailId, mtoken);
		commonFunction.validateEmailLoginUser(request);
		HashMap<String, Object> response = loginService.googleEmailOtpVerify(request);
		Boolean status = (Boolean) response.get("success");

		if (Boolean.TRUE.equals(status)) {
			return ResponseEntity.ok(response);
		}

		return ResponseEntity.badRequest().body(response);
	}

	
	
	

	


}
