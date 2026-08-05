package com.authencation_service.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authencation_service.config.JwtUtil;
import com.authencation_service.dto.MFStackOtpDto;
import com.authencation_service.helpers.Helpers;
import com.authencation_service.reposatory.MobileTokenRepo;
import com.authencation_service.reposatory.UserOtpRepository;
import com.authencation_service.services.RegistrationService;



@RestController
@RequestMapping("/api/auth")
public class RegistrationController {
	

	@Autowired
	private RegistrationService mfAuthService;
	@Autowired
	private UserOtpRepository otpRepo;
	
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private Helpers commonFunction;

	@Autowired
	private MobileTokenRepo tokenRepo;
	
	@Autowired
	private CommonController commonMethod;

	@PostMapping("/registration/generate_token")
	public ResponseEntity<?> generateToken(@RequestBody MFStackOtpDto request,
			@RequestHeader(value = "phone_no", required = true) String phoneNo)
			throws RuntimeException, BadRequestException,Exception {
		String headerPhoneNo = phoneNo= commonMethod.decrypted(request).get("phoneNo");
		System.err.println("Phone no "+phoneNo);
		
		System.err.println(headerPhoneNo);
		request.setPhone_no(headerPhoneNo);
		String finalIdentifier = Optional.ofNullable(phoneNo).map(String::trim).filter(s -> !s.isBlank())
				.orElseThrow(() -> new RuntimeException("Something went wrong!!"));
		String mobileNo = Optional.ofNullable(request.getPhone_no()).map(String::trim).orElse("");
		if (mobileNo.isEmpty()) {
			throw new BadRequestException("Mobile number is required");
		}
		// Numeric validation
		if (!commonFunction.isNumeric(mobileNo)) {
			throw new BadRequestException("Mobile no must contain only numbers");
		}
		if (mobileNo.length() != 10) {
			throw new BadRequestException("Mobile no must be 10 digit");
		}
		
		Optional.of(finalIdentifier).filter(id -> id.equals(mobileNo))
				.orElseThrow(() -> new RuntimeException("Something went wrong!"));

		Map<String, Object> data = mfAuthService.generateAccessToken(request);
		return ResponseEntity.ok(Map.of("success", true, "message", "Token generate successfully", "data", data));
	}

	@PostMapping("/registration/mobile_otp")
	public ResponseEntity<?> sendMobileOtp(@RequestBody MFStackOtpDto request,
			@RequestHeader(value = "token", required = true) String token,
			@RequestHeader(value = "phone_no", required = true) String phoneNo) throws BadRequestException,Exception {
		
		
		System.err.println(request.getIsdcode());
		request.setHeaderPhoneNo(phoneNo);
		String headerPhone = commonMethod.decrypted(request).get("headerPhoneNo");
		//System.err.println(headerPhone);
		String bodyPhoneNo = commonMethod.decrypted(request).get("phoneNo");
		String bodyIsdcode = commonMethod.decrypted(request).get("isdCode");
		
//		System.err.println("Body Phone no "+ bodyPhoneNo);
//		System.err.println("Body isd code "+ bodyIsdcode);		
		request.setPhone_no(bodyPhoneNo);
		request.setIsdcode(bodyIsdcode);
		String apiToken = Optional.ofNullable(token).map(String::trim).filter(s -> !s.isBlank()).orElseThrow(() -> new BadRequestException("Token is required"));
		commonFunction.validateRequest(Map.of("Token", token, "Phone number", headerPhone));
		commonFunction.validateMobileNumber(request);
		commonFunction.validatePhoneNumber(headerPhone,bodyPhoneNo);
		request.setToken(token);
		String mtoken = request.getToken();
		boolean isValid = commonFunction.validateToken(bodyPhoneNo, mtoken);
		Optional.of(isValid).filter(Boolean::booleanValue).orElseThrow(() -> new RuntimeException("Token is invalid or expired"));
		tokenRepo.updateTokenStatus(headerPhone, mtoken);
		HashMap<String, Object> response;
		response = response = mfAuthService.SendMobileOtp(request);
		Boolean status = (Boolean) response.get("success");
		if (Boolean.TRUE.equals(status)) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.badRequest().body(response);
	}

	@PostMapping("/registration/mobile_otp_verify")
	public ResponseEntity<?> mobileOtpVerify(@RequestBody MFStackOtpDto request,
			@RequestHeader(value = "token", required = true) String token,
			@RequestHeader(value = "phone_no", required = true) String phoneNo) throws BadRequestException,Exception {
	
		request.setHeaderPhoneNo(phoneNo);
		String headerPhone = commonMethod.decrypted(request).get("headerPhoneNo");
		String bodyPhoneNo = commonMethod.decrypted(request).get("phoneNo");
		String bodyIsdcode = commonMethod.decrypted(request).get("isdCode");
		String bodyPhoneOtp = commonMethod.decrypted(request).get("otp");
	//	System.out.println(bodyPhoneOtp);
		request.setOtp(bodyPhoneOtp);
		request.setPhone_no(bodyPhoneOtp);
		request.setIsdcode(bodyIsdcode);
//		request.setIdentifier(bodyIsdcode);
		
		commonFunction.validateRequest(Map.of("Token", token, "Phone number", headerPhone));
		request.setPhone_no(bodyPhoneNo);
		commonFunction.validateMobileNumber(request);
		commonFunction.validatePhoneNumber(headerPhone,bodyPhoneNo);
		request.setToken(token);
		String mtoken = request.getToken();
		boolean isValid = 		commonFunction.validateToken(bodyPhoneNo, mtoken);

		Optional.of(isValid).filter(Boolean::booleanValue)
				.orElseThrow(() -> new RuntimeException("Token is invalid or expired"));
		tokenRepo.updateTokenStatus(phoneNo, mtoken);
	
		HashMap<String, Object> response = mfAuthService.mobileOtpVerify(request);
		Boolean status = (Boolean) response.get("success");
		Integer flag = (Integer) response.get("flag");
		if (Boolean.TRUE.equals(status) || Integer.valueOf(1).equals(flag)) {
			response.remove("flag");
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.badRequest().body(response);
	}
//
	@PostMapping("/registration/email_otp")
	public ResponseEntity<?> sendEmailOtp(@RequestBody MFStackOtpDto request,
			@RequestHeader(value = "token", required = true) String token,@RequestHeader(value = "phone_no", required = true) String phoneNo) throws Exception {

		request.setHeaderPhoneNo(phoneNo);
		
		String headerPhone = commonMethod.decrypted(request).get("headerPhoneNo");
		String bodyEmailId = commonMethod.decrypted(request).get("emailId");
		String bodySiteUserId = commonMethod.decrypted(request).get("siteUserId");
		
	//	System.err.println(bodySiteUserId  +" ++ ");
		
		request.setToken(token);
		request.setEmail_id(bodyEmailId);
		request.setSite_user_id(bodySiteUserId);
		String mtoken = request.getToken();
		System.err.println("Site user id  " + bodySiteUserId);
		commonFunction.validateRequest(Map.of("Token", token, "Phone number", headerPhone));
		
		boolean isValid = 		commonFunction.validateToken(headerPhone, mtoken);

		Optional.of(isValid).filter(Boolean::booleanValue)
				.orElseThrow(() -> new RuntimeException("Token is invalid or expired"));
		tokenRepo.updateTokenStatus(headerPhone, mtoken);
		
		
		
		HashMap<String, Object> response = mfAuthService.SendEmailOtp(request);
	
		Boolean status = (Boolean) response.get("success");
		if (Boolean.TRUE.equals(status)) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.badRequest().body(response);

	}

	@PostMapping("/registration/email_otp_verify")
	public ResponseEntity<?> emailOtpVerify(@RequestBody MFStackOtpDto request,
			@RequestHeader(value = "token", required = true) String token,
			@RequestHeader(value = "phone_no", required = true) String phoneNo) throws BadRequestException,Exception {
		request.setPhone_no(phoneNo);
		String headerPhone = commonMethod.decrypted(request).get("phoneNo");
		String bodyEmailId = commonMethod.decrypted(request).get("emailId");
		String bodySiteUserId = commonMethod.decrypted(request).get("siteUserId");
		String bodyEmailIdOtp = commonMethod.decrypted(request).get("otp");
		commonFunction.validateRequest(Map.of("Token", token, "Phone number", headerPhone));
		request.setToken(token);
		request.setEmail_id(bodyEmailId);
		request.setOtp(bodyEmailIdOtp);
		request.setSite_user_id(bodySiteUserId);
	
		String mtoken = request.getToken();
		boolean isValid = 		commonFunction.validateToken(headerPhone, mtoken);

		Optional.of(isValid).filter(Boolean::booleanValue)
				.orElseThrow(() -> new RuntimeException("Token is invalid or expired"));
		tokenRepo.updateTokenStatus(headerPhone, mtoken);
		HashMap<String, Object> response = mfAuthService.emailOtpVerify(request);
		Boolean status = (Boolean) response.get("success");
		Integer flag = (Integer) response.get("flag");
		if (Boolean.TRUE.equals(status) || Integer.valueOf(1).equals(flag)) {
			response.remove("flag");
			return ResponseEntity.ok(response);
		}

		return ResponseEntity.badRequest().body(response);
	}

	@PostMapping("/registration/name_update")
	public ResponseEntity<?> nameUpdateSiteUserLogin(@RequestBody MFStackOtpDto request,
			@RequestHeader(value = "token", required = true) String token,
			@RequestHeader(value = "phone_no", required = true) String phoneNo) throws BadRequestException ,Exception {
		request.setPhone_no(phoneNo);
		String headerPhone = commonMethod.decrypted(request).get("phoneNo");
	
		String bodySiteUserId = commonMethod.decrypted(request).get("siteUserId");
		String bodyName = commonMethod.decrypted(request).get("name");
		
		request.setSite_user_id(bodySiteUserId);
		request.setName(bodyName);
		commonFunction.validateRequest(Map.of("Token", token, "Phone number", headerPhone));
		request.setToken(token);
		String mtoken = request.getToken();
		boolean isValid = 		commonFunction.validateToken(headerPhone.trim(), mtoken.trim());
		Optional.of(isValid).filter(Boolean::booleanValue)
				.orElseThrow(() -> new RuntimeException("Token is invalid or expired"));
		tokenRepo.updateTokenStatus(headerPhone, mtoken);
		HashMap<String, Object> response = mfAuthService.updateName(request);
		Boolean status = (Boolean) response.get("success");
		if (Boolean.TRUE.equals(status)) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.badRequest().body(response);
	}

	@PostMapping("/registration/email_update_google")
	public ResponseEntity<?> googleEmailUpdate(@RequestBody MFStackOtpDto request,
			@RequestHeader(value = "token", required = true) String token,
			@RequestHeader(value = "phone_no", required = true) String phoneNo) throws BadRequestException,Exception {
		
		
		request.setPhone_no(phoneNo);
		String headerPhone = commonMethod.decrypted(request).get("phoneNo");
		String bodyEmailId = commonMethod.decrypted(request).get("emailId");
		String bodySiteUserId = commonMethod.decrypted(request).get("siteUserId");
		
		
		
		commonFunction.validateRequest(Map.of("Token", token, "Phone number", headerPhone));
		request.setToken(token);
		request.setEmail_id(bodyEmailId);
		
		request.setSite_user_id(bodySiteUserId);
		String mtoken = request.getToken();
		boolean isValid = 		commonFunction.validateToken(headerPhone.trim(), mtoken.trim());

		Optional.of(isValid).filter(Boolean::booleanValue)
				.orElseThrow(() -> new RuntimeException("Token is invalid or expired"));
		tokenRepo.updateTokenStatus(headerPhone.trim(), mtoken.trim());
		HashMap<String, Object> response = mfAuthService.googleEmailOtpVerify(request);
		Boolean status = (Boolean) response.get("success");
		if (Boolean.TRUE.equals(status)) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.badRequest().body(response);
	}

	

	
}
