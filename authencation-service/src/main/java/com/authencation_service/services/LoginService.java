package com.authencation_service.services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


import com.authencation_service.config.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.authencation_service.client.SiteUserFeignClient;
import com.authencation_service.config.JwtUtil;
import com.authencation_service.dto.MFStackOtpDto;
import com.authencation_service.entity.LoginTokenEntity;
import com.authencation_service.entity.UserOtp;
import com.authencation_service.helpers.Helpers;
import com.authencation_service.reposatory.LoginTokenRepo;
import com.authencation_service.reposatory.MFStackOtpRepo;
import com.authencation_service.reposatory.UserOtpRepository;

import jakarta.transaction.Transactional;

@Service
public class LoginService {
//	@Value("${OTPExpireTime}")
//	private int otpExpiryMinutes;
//	
//	@Value("${TokenExpireTime}")
//	private int tokenExpireTime;

	private int otpExpiryMinutes = 5;

	private int tokenExpireTime = 5;

	@Autowired
	private UserOtpRepository otpRepo;

	@Autowired
	private MFStackOtpRepo mfOtpService;

	@Autowired
	private SiteUserFeignClient client;

	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private LoginTokenRepo tokenrepo;

	@Autowired
	private Helpers common;

	public Map<String, Object> generateAccessToken(MFStackOtpDto request) {
		Map<String, Object> result = new HashMap<>();
		String identifier = request.getIdentifier();
		LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(tokenExpireTime);
		Timestamp expiry = Timestamp.valueOf(expiryTime);
		String jwtToken = jwtUtil.createAccessToken(identifier, expiryTime);
		LoginTokenEntity tokenEntity = tokenrepo.findByIdentifier(identifier).stream().findFirst().orElse(null);
		if (tokenEntity != null) {
			tokenEntity.setToken(jwtToken);
			tokenEntity.setExpiresAt(expiry);
		} else {
			tokenEntity = new LoginTokenEntity();
			tokenEntity.setIdentifier(identifier);
			tokenEntity.setToken(jwtToken);
			tokenEntity.setExpiresAt(expiry);
			tokenEntity.setCreatedAt(new Timestamp(System.currentTimeMillis()));
			tokenEntity.setStatus(1);
		}
		tokenrepo.save(tokenEntity);
		result.put("ACCESS_TOKEN", jwtToken);
		return result;
	}

	@Transactional
	public HashMap<String, Object> SendMobileOtp(MFStackOtpDto request) throws Exception {
		HashMap<String, Object> map = new HashMap<>();
		String phoneNo = request.getPhone_no();
		String isdCode = request.getIsdcode();
		String fullMobileNo = isdCode + phoneNo;
		client.validateLogin(request);
		String otp = generateMFOTP(phoneNo, isdCode);
		map.put("success", true);
		map.put("data", Map.of("otp", otp));
		map.put("message", "Mobile OTP sent successfully");
		return map;
	}

	@Transactional
	public HashMap<String, Object> mobileOtpVerify(MFStackOtpDto request) throws BadRequestException {
		HashMap<String, Object> map = new HashMap<>();
		String phoneNo = request.getPhone_no();
		String countryCode = Optional.ofNullable(request.getIsdcode()).map(String::trim).orElse("");
		String otp = Optional.ofNullable(request.getOtp()).map(String::trim).filter(s -> !s.isBlank())
				.filter(s -> s.length() >= 6).orElseThrow(() -> new RuntimeException("OTP must be at least 6 digits"));
		phoneNo = countryCode + phoneNo;

		String otpValue = phoneNo;
		Timestamp now = new Timestamp(System.currentTimeMillis());

		UserOtp record = otpRepo.findTopByIdentifierOrderByCreatedAtDesc(otpValue).stream().findFirst()
				.orElseThrow(() -> new BadRequestException("OTP not found"));

		// OTP Validation
		if (!otp.equals(record.getOtp())) {
			map.put("flag", 1);
			map.put("success", false);
			map.put("message", "Invalid OTP");
			return map;
		}

		// Expiry Validation
//		if (record.getExpiresAt().before(now)) {
//			throw new BadRequestException("OTP is expired");
//		}
		Optional<UserOtp> otpData = otpRepo.verifyOtp(phoneNo, otp.trim());
		if (!otpData.isPresent()) {
			throw new BadRequestException("OTP is expired");
		}
		record.setVerified("Y");
		otpRepo.save(record);
		return client.verifyMobileOtp(request);
	}

	@Transactional
	public HashMap<String, Object> SendEmailOtp(MFStackOtpDto request) throws Exception {
		HashMap<String, Object> map = new HashMap<>();
		String EmailId = request.getEmail_id();
		String identifier = EmailId;

		client.validateLogin(request);

		String otp = generateMFOTP(identifier, "");
		// common.sendOtpMailAsync(identifier, "User", otp, request.getRequest());
		map.put("success", true);
		map.put("data", Map.of("otp", otp));
		map.put("message", "Email OTP sent successfully");
		return map;
	}

	@Transactional
	public HashMap<String, Object> emailOtpVerify(MFStackOtpDto request) throws BadRequestException {
		HashMap<String, Object> map = new HashMap<>();
		String emailId = request.getEmail_id();

//		 if (!isEmail(emailId)) {
//		        throw new BadRequestException("Invalid Email Id");
//		    }
		String otp = Optional.ofNullable(request.getOtp()).map(String::trim).filter(s -> !s.isBlank())
				.filter(s -> s.length() >= 6).orElseThrow(() -> new RuntimeException("OTP must be at least 6 digits"));

		String otpValue = emailId;
		Timestamp now = new Timestamp(System.currentTimeMillis());
		// UserOtp record =
		// otpRepo.findTopByIdentifierOrderByCreatedAtDesc(otpValue).stream().filter(r
		// -> r.getExpiresAt().after(now)).filter(r ->
		// otp.equals(r.getOtp())).findFirst().orElseThrow(() -> new
		// BadRequestException("OTP is expired"));
		UserOtp record = otpRepo.findTopByIdentifierOrderByCreatedAtDesc(otpValue).stream().findFirst()
				.orElseThrow(() -> new BadRequestException("OTP not found"));

		// OTP Validation
		if (!otp.equals(record.getOtp())) {
			map.put("flag", 1);
			map.put("success", false);
			map.put("message", "Invalid OTP");
			return map;
		}

		Optional<UserOtp> otpData = otpRepo.verifyOtp(emailId.trim(), otp.trim());
		if (!otpData.isPresent()) {
			throw new BadRequestException("OTP is expired");
		}

		record.setVerified("Y");
		otpRepo.save(record);
		return client.verifyEmailOtp(request);
	}

	@Transactional
	public String generateMFOTP(String identifier, String countryCode) throws Exception {
//		String otp = common.getRandonNumberString(6);
		String otp = "123456";
		String smsText = "Your PROSPUR FINTECH OTP is " + otp
				+ ". Use this code to verify your number and complete your registration. Do not share this OTP with anyone.";
//		String smsResponse = smsService.sendsms(identifier, smsText);
		UserOtp entity = new UserOtp();
		if (!common.isEmail(identifier)) {
			identifier = countryCode + identifier;
		}
		// System.err.print(identifier);
		entity.setIdentifier(identifier);
		entity.setOtp(otp);
		entity.setVerified("N");
		entity.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		entity.setExpiresAt(Timestamp.valueOf(LocalDateTime.now().plusMinutes(otpExpiryMinutes)));
		otpRepo.save(entity);
		return otp;
	}

	@Transactional
	public HashMap<String, Object> googleEmailOtpVerify(MFStackOtpDto request) throws BadRequestException {
		HashMap<String, Object> map = new HashMap<>();
		return client.googleLogin(request);
	}

}
