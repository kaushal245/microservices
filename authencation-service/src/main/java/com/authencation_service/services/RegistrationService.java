package com.authencation_service.services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;



import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.authencation_service.client.SiteUserFeignClient;
import com.authencation_service.config.JwtUtil;
import com.authencation_service.dto.MFStackOtpDto;
import com.authencation_service.dto.siteUserResponse;
import com.authencation_service.entity.MobileToken;
import com.authencation_service.entity.UserOtp;
import com.authencation_service.helpers.Helpers;
import com.authencation_service.reposatory.MobileTokenRepo;
import com.authencation_service.reposatory.UserOtpRepository;

import jakarta.transaction.Transactional;


@Service
public class RegistrationService {

	
	
	private int otpExpiryMinutes = 5;

	
	private int tokenExpireTime = 5;

	
	
	@Autowired
	private UserOtpRepository otpRepo;

	@Autowired
	private Helpers commonFunction;

	

	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private MobileTokenRepo tokenrepo;

	@Autowired
	private Helpers common;

	
	@Autowired
	private SiteUserFeignClient client;
	
	
	private static Timestamp now = new Timestamp(System.currentTimeMillis());


	@Transactional
	public HashMap<String, Object> SendMobileOtp(MFStackOtpDto request) throws Exception {
		HashMap<String, Object> map = new HashMap<>();
		String phoneNo = request.getPhone_no();
		
		String identifier = phoneNo;
		System.err.println("000"+identifier);
		System.err.println("000110"+request.getIsdcode());
		
		
//		System.err.println("SSSSSSSSSSSSSSSSSSSSSSSS "+identifier);
//		System.err.println("SSSSSSSSSSSSSSSSSSSSSSSS "+request.getOtp());
//		 SiteUserLogin user = siteUserRepo.findBySiteMobileNo(phoneNo).stream().findFirst().orElseThrow(() -> new BadRequestException("User does not exist"));
//		 boolean emailExists = siteUserRepo
//		            .findByMobileOrEmailAndStatus1(identifier)
//		            .stream()
//		            .anyMatch(data ->!Objects.equals(data.getUserId(),user.getUserId()));
//		    
//		    if (emailExists) {
//		        throw new BadRequestException("");
//		    }
		
		
		String otp = generateMFOTP(identifier, request.getIsdcode());
		map.put("success", true);
		map.put("message", "Mobile OTP sent successfully");
		map.put("data", Map.of("otp", otp));
		return map;
	}

	@Transactional
	public HashMap<String, Object> mobileOtpVerify(MFStackOtpDto request) throws BadRequestException {
		
		HashMap<String, Object> map = new HashMap<>();

		String phoneNo = request.getPhone_no();
		String countryCode = Optional.ofNullable(request.getIsdcode()).map(String::trim).orElse("");

		phoneNo = countryCode + phoneNo;
		request.setPhone_no(phoneNo);
		String otp = Optional.ofNullable(request.getOtp()).map(String::trim).filter(s -> !s.isBlank())
				.filter(s -> s.length() >= 6).orElseThrow(() -> new RuntimeException("OTP must be at least 6 digits"));

		Timestamp now = new Timestamp(System.currentTimeMillis());

		// ================= OTP FETCH =================
		UserOtp record = otpRepo.findTopByIdentifierOrderByCreatedAtDesc(phoneNo.trim()).stream().findFirst()
				.orElseThrow(() -> new BadRequestException("OTP not found"));
		
		
		
		// ================= OTP VALIDATION =================
		if (!otp.equals(record.getOtp())) {
			map.put("success", false);
			map.put("message", "Invalid OTP " + record.getOtp());
			map.put("flag", 1);
			return map;
		}
		
		Optional<UserOtp> otpData = otpRepo.verifyOtp(phoneNo.trim(), otp);

		if (!otpData.isPresent()) {
		    throw new BadRequestException("OTP is expired");
		}
		
//		if (record.getExpiresAt().before(now)) {
//			throw new BadRequestException("OTP is expired");
//		}
		record.setVerified("Y");
		otpRepo.save(record);
		HashMap<String,Object> userResponse = client.verifyMobile(request);
		
		
		return userResponse;
	}
//
	@Transactional
	public HashMap<String, Object> SendEmailOtp(MFStackOtpDto request) throws Exception {
		HashMap<String, Object> map = new HashMap<>();
//		System.err.println("Email = " + request.getEmail_id());
//		System.err.println("Site User Id = " + request.getSite_user_id());
		String EmailId = Optional.ofNullable(request.getEmail_id().trim()).map(String::trim).filter(s -> !s.isBlank())
				.orElseThrow(() -> new BadRequestException("Email Id is required"));
		String identifier = EmailId;
		if (!common.isEmail(identifier)) {
			throw new BadRequestException("Invalid Email Id");
		}

		if (!common.isValidEmail(identifier)) {
			throw new BadRequestException("Only valid email id should be allowed to enter");
		}

		
		
	    siteUserResponse user =
	    		client.getUser(
	                    Integer.valueOf(request.getSite_user_id())
	            );


	    if(user == null){
	        throw new BadRequestException(
	                "User does not exist"
	        );
	    }



	    if(user.getMStatus()!=null && user.getMStatus()>2){

	        throw new BadRequestException(
	                "This user is already registered"
	        );
	    }



	    // CHECK EMAIL EXISTS FROM SITE USER SERVICE

	    HashMap<String, Object> emailExists =
	            client.checkEmail(request);



	    if(Boolean.TRUE.equals(emailExists)){

	        throw new BadRequestException(
	                "Email Id already exists"
	        );

	    }



	    String otp = generateMFOTP(EmailId,"");


//	    common.sendOtpMailAsync(
//	            EmailId,
//	            "User",
//	            otp,
//	            request.getRequest()
//	    );


	    map.put("success",true);

	    map.put("data",
	            Map.of("otp",otp)
	    );

	    map.put(
	            "message",
	            "Email OTP sent successfully"
	    );


	    return map;
	}

	@Transactional
	public HashMap<String, Object> emailOtpVerify(MFStackOtpDto request) throws BadRequestException {

		HashMap<String, Object> map = new HashMap<>();

		String emailId = Optional.ofNullable(request.getEmail_id()).map(String::trim).filter(s -> !s.isBlank())
				.orElseThrow(() -> new BadRequestException("Email Id is required"));

		if (!common.isEmail(emailId)) {
			throw new BadRequestException("Invalid Email Id");
		}

		String otp = Optional.ofNullable(request.getOtp()).map(String::trim).filter(s -> !s.isBlank())
				.orElseThrow(() -> new BadRequestException("OTP is required"));

		if (otp.length() != 6) {

			throw new BadRequestException("OTP must be 6 digits");
		}

		// GET USER FROM SITE USER SERVICE

		siteUserResponse user = client.getUser(Integer.valueOf(request.getSite_user_id()));

		if (user == null) {

			throw new BadRequestException("User not found");
		}

		// OTP CHECK

		UserOtp record = otpRepo.findTopByIdentifierOrderByCreatedAtDesc(emailId).stream().findFirst()
				.orElseThrow(() -> new BadRequestException("OTP not found"));

		if (!otp.equals(record.getOtp())) {

			map.put("success", false);
			map.put("flag", 1);
			map.put("message", "Invalid OTP");

			return map;
		}

		Optional<UserOtp> otpData = otpRepo.verifyOtp(emailId, otp);

		if (otpData.isEmpty()) {

			throw new BadRequestException("OTP is expired");

		}

		record.setVerified("Y");
		otpRepo.save(record);
		
		Integer status = user.getMStatus();

		// ALREADY VERIFIED

		if (status != null && (status == 2 || status == 3)) {

			map.put("success", true);

			map.put("message", "Email OTP verified successfully");

			map.put("data", Map.of( "site_user_id",
					user.getSite_user_id(), "mf_status", status));

			return map;
		}
		// UPDATE EMAIL THROUGH SITE USER SERVICE
		HashMap<String, Object> updatedUser = client.verifyEmail(request);
		return updatedUser;

	}

	@Transactional
	public HashMap<String, Object> updateName(MFStackOtpDto request) throws BadRequestException {

		HashMap<String, Object> map = new HashMap<>();

		String name = Optional.ofNullable(request.getName()).map(String::trim).filter(s -> !s.isBlank())
				.orElseThrow(() -> new BadRequestException("Name is required"));

		if (!name.matches("^[a-zA-Z0-9 ]+$")) {

			throw new BadRequestException("In name field special character should not be allowed");
		}

		// GET USER FROM SITE USER SERVICE

		siteUserResponse user = client.getUser(Integer.valueOf(request.getSite_user_id()));
		System.err.println("SSSS"+ user);
		if (user == null) {

			throw new BadRequestException("User does not exist");
		}

		// ALREADY COMPLETED

		if (user.getMStatus() != null && user.getMStatus() == 3) {

			map.put("success", true);

			map.put("data", Map.of("name", user.getName() != null ? user.getName() : "", "site_user_id",
					user.getSite_user_id(), "mf_status", user.getMStatus()));

			map.put("message", "Login successfully");

			return map;
		}

		// UPDATE NAME IN SITE USER SERVICE

		

		HashMap<String, Object> updatedUser = client.updateName(request);

		

		return updatedUser;
	}

	private String getStepDescription(Integer status) {
		return Stream
				.of(Map.entry(1, "Mobile OTP verified successfully"), Map.entry(2, "Email OTP verified successfully"),
						Map.entry(3, "Login successfully"), Map.entry(4, "Sent to MFSTACK successfully"),
						Map.entry(5, "Verified by MFSTACK successfully"))
				.filter(entry -> entry.getKey().equals(status)).map(Map.Entry::getValue).findFirst()
				.orElse("Verification pending");
	}
//
	public String generateMFOTP(String identifier, String countryCode) throws Exception {
		System.err.println("Check this 1 " + countryCode);
		System.err.println("Check this 2 " + identifier);
//		String otp = common.getRandonNumberString(6);
		String otp = "123456";
		String smsText = "Your PROSPUR FINTECH OTP is " + otp
				+ ". Use this code to verify your number and complete your registration. Do not share this OTP with anyone.";
	//	String smsResponse = smsService.sendsms(identifier, smsText);
		UserOtp entity = new UserOtp();
		if (!commonFunction.isEmail(identifier)) {
			identifier = countryCode + identifier;
		}
	//	System.err.println(" otp method  "+identifier);
		
		System.err.println("Check this" + identifier);
		entity.setIdentifier(identifier);
		entity.setOtp(otp);
		entity.setVerified("N");
		entity.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		entity.setExpiresAt(Timestamp.valueOf(LocalDateTime.now().plusMinutes(otpExpiryMinutes)));
		otpRepo.save(entity);
		return otp;
	}

//
	@Transactional
	public HashMap<String, Object> googleEmailOtpVerify(MFStackOtpDto request) throws BadRequestException {
		HashMap<String, Object> map = new HashMap<>();

		String emailId = Optional.ofNullable(request.getEmail_id().trim()).map(String::trim).filter(s -> !s.isBlank())
				.orElseThrow(() -> new BadRequestException("Email Id is required"));

		if (!common.isEmail(emailId)) {
			throw new BadRequestException("Invalid Email Id");
		}

		if (!common.isValidEmail(emailId)) {
			throw new BadRequestException("Only valid email id should be allowed to enter");
		}

		String name = Optional.ofNullable(request.getName().trim()).map(String::trim).filter(s -> !s.isBlank())
				.orElseThrow(() -> new BadRequestException("Name is required"));

		HashMap<String, Object> updateEmailWithGoogle = client.updateEmailWithGoogle(request);

		return updateEmailWithGoogle;

	}

	public Map<String, Object> generateAccessToken(MFStackOtpDto request) throws RuntimeException {
		Map<String, Object> result = new HashMap<>();
		LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(tokenExpireTime);
		Timestamp expiry = Timestamp.valueOf(expiryTime);
		String jwtToken = jwtUtil.createAccessToken(request.getPhone_no(), expiryTime);

		// Get latest active token
		MobileToken token = tokenrepo.findByMobileNo(request.getPhone_no().trim()).stream()
				.filter(data -> data.getStatus() != null && data.getStatus() == 1)
				.sorted((a, b) -> b.getTokenId().compareTo(a.getTokenId())).findFirst().orElse(null);
		// Create new token if no active record found
		if (token == null) {
			token = new MobileToken();
			token.setMobileNo(request.getPhone_no());
			token.setStatus(1);
		}
		// Update token
		token.setToken(jwtToken);
		token.setExpiry(expiry);
		tokenrepo.save(token);
		result.put("ACCESS_TOKEN", jwtToken);
		return result;
	}
//
	public Map<String, Object> generateToken(MFStackOtpDto request) {
		Map<String, Object> result = new HashMap<>();
		LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);
		Timestamp expiry = Timestamp.valueOf(expiryTime);
		String jwtToken = jwtUtil.createAccessToken("", expiryTime);
		MobileToken token = new MobileToken();
		token.setToken(jwtToken);
		token.setExpiry(expiry);
		token.setStatus(1);
		tokenrepo.save(token);
		result.put("ACCESS_TOKEN", jwtToken);
		return result;
	}
}
