package com.blog_site_user.services;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.blog_site_user.config.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog_site_user.dto.MobileVerifyRequest;
import com.blog_site_user.dto.SiteUserLoginDTO;
import com.blog_site_user.dto.SiteUserResponse;
import com.blog_site_user.entity.SiteUserLogin;
import com.blog_site_user.reposatory.SiteUserLoginReco;

import jakarta.transaction.Transactional;

@Service
public class SiteUserService {
	
	@Autowired
	private SiteUserLoginReco siteUserRepo;
	
	@Transactional
	public HashMap<String, Object> verifyMobile(MobileVerifyRequest request) {

		HashMap<String, Object> map = new HashMap<>();

		String phoneNo = request.getPhone_no().trim();
		Timestamp now = new Timestamp(System.currentTimeMillis());

		SiteUserLogin user = siteUserRepo.findByMobileOrEmailAndStatus1(phoneNo.trim()).stream().findFirst()
				.orElse(null);

		System.err.print("check ================================" + user);
		// NEW USER
		if (user == null) {

			user = new SiteUserLogin();
			user.setMobileNo(phoneNo);
			user.setStatus((short) 1);
			user.setMStatus(1);
			user.setType(3);
			user.setRegDate(now);

			siteUserRepo.save(user);

			map.put("success", true);
			map.put("message", "Mobile OTP verified successfully");
			map.put("data", Map.of("mf_status", user.getMStatus(), "site_user_id", user.getUserId(), "name", ""));

			return map;
		}

		// Already verified
		if (user.getMStatus() != null && user.getMStatus() > 1) {

			map.put("success", true);
			map.put("message", "Mobile OTP already verified");
			map.put("data", Map.of("mf_status", user.getMStatus(), "site_user_id", user.getUserId(), "name",
					user.getName() == null ? "" : user.getName()));

			return map;
		}

		// Update existing user
		user.setMobileNo(phoneNo);
		user.setMStatus(1);
		user.setModDate(now);

		siteUserRepo.save(user);

		map.put("success", true);
		map.put("message", "Mobile verified successfully");
		map.put("data", Map.of("mf_status", user.getMStatus(), "site_user_id", user.getUserId(), "name",
				user.getName() == null ? "" : user.getName()));

		return map;
	}
	
	
	@Transactional
	public HashMap<String,Object> checkEmail(
			MobileVerifyRequest request)  {


	    HashMap<String,Object> map = new HashMap<>();

	    boolean emailExists = siteUserRepo.findByMobileOrEmailAndStatus1( request.getEmail_id().trim()).stream()
//	            .filter(data -> data.getMStatus() != null
//                && (data.getMStatus() == 1 ||data.getMStatus() == 2 ||data.getMStatus() == 3 || data.getMStatus() == 4 || data.getMStatus() == 5))
//			.anyMatch(data -> !Objects.equals(data.getUserId(), user.getUserId()));
			.findAny().isPresent();
	 

	    if(emailExists){
	        throw new com.blog_site_user.config.BadRequestException(
	            "Email Id already exists"
	        );
	    }


	    map.put("success",true);
	    map.put("message","Email available");

	    return map;
	}

	@Transactional
	public HashMap<String, Object> verifyEmail(MobileVerifyRequest request) throws BadRequestException {

		HashMap<String, Object> map = new HashMap<>();

		SiteUserLogin user = siteUserRepo.findBySiteUserId(Integer.valueOf(request.getSite_user_id())).stream().findFirst()
				.orElseThrow(() -> new BadRequestException("User not found"));

		Integer status = user.getMStatus();

		if (status != null && (status == 2 || status == 3)) {

			map.put("success", true);

			map.put("data", Map.of("site_user_id", user.getUserId(),

					"mf_status", status,

					"name", user.getName() == null ? "" : user.getName()));

			return map;
		}

		user.setEmail(request.getEmail_id());
		user.setMStatus(2);
		user.setModDate(new Timestamp(System.currentTimeMillis()));

		siteUserRepo.save(user);

		map.put("success", true);

		map.put("message", "Email verified successfully");

		map.put("data", Map.of("site_user_id",request.getSite_user_id(),

				"mf_status", user.getMStatus()));
		System.err.println(map);
		
		return map;

	}
	
	@Transactional
	public HashMap<String, Object> updateName(MobileVerifyRequest request)  {

		HashMap<String, Object> map = new HashMap<>();
		System.err.print("Working   "+request.getSite_user_id());
		SiteUserLogin user = siteUserRepo.findBySiteUserId(Integer.valueOf(request.getSite_user_id())).stream().findFirst()
				.orElseThrow(() -> new BadRequestException("User not found"));
		
		System.err.print(user);

		if (user.getMStatus() != null && user.getMStatus() == 3) {

			map.put("success", true);

			map.put("message", "Login successfully");

			return map;
		}

		user.setName(request.getName());
		user.setMStatus(3);
		user.setModDate(new Timestamp(System.currentTimeMillis()));

		siteUserRepo.save(user);

		map.put("success", true);
		map.put("message", "Login successfully");

		map.put("data", Map.of("site_user_id", request.getSite_user_id(),

				"mf_status", user.getMStatus(),

				"name", user.getName()));

		System.err.println("SSS" + map);
		return map;

	}
	
	public SiteUserResponse getUser(Integer userId) {

	    SiteUserLogin user = siteUserRepo
	            .findBySiteUserId(userId)
	            .stream()
	            .findFirst()
	            .orElseThrow(() ->
	                    new BadRequestException("User not found")
	            );


	    SiteUserResponse response = new SiteUserResponse();

	    response.setSite_user_id(user.getUserId());
	    response.setName(
	            user.getName() == null ? "" : user.getName()
	    );
	    response.setEmail(user.getEmail());
	    response.setMobileNo(user.getMobileNo());
	    response.setMStatus(user.getMStatus());
	    
	    System.err.print(response.toString());
	    return response;
	}
	
	@Transactional
	public HashMap<String, Object> googleEmailVerify(MobileVerifyRequest request) throws BadRequestException {

		HashMap<String, Object> map = new HashMap<>();

		SiteUserLogin user = siteUserRepo.findBySiteUserId(Integer.valueOf(request.getSite_user_id())).stream()
				.findFirst().orElseThrow(() -> new BadRequestException("User does not exists"));

		if (user.getMStatus() != null && user.getMStatus() > 2) {
			throw new BadRequestException("This user already register!!");
		}

		boolean emailExists = siteUserRepo.findByMobileOrEmailAndStatus1(request.getEmail_id().trim()).stream()
//				.anyMatch(data -> !Objects.equals(data.getUserId(), user.getUserId()));
				.findAny().isPresent();

		if (emailExists) {
			throw new BadRequestException("Email Id already exists");
		}

		Timestamp now = new Timestamp(System.currentTimeMillis());

		if (user.getMStatus() != null && (user.getMStatus() == 2 || user.getMStatus() == 3)) {

			map.put("success", true);

			map.put("data", Map.of("name", user.getName() == null ? "" : user.getName(), "site_user_id",
					user.getUserId(), "mf_status", user.getMStatus()));

			map.put("message", "Email already verified");

			return map;
		}

		user.setEmail(request.getEmail_id());
		user.setName(request.getName());
		user.setModDate(now);
		user.setMStatus(2);

		siteUserRepo.save(user);

		map.put("success", true);

		map.put("data",
				Map.of("site_user_id", user.getUserId(), "mf_status", user.getMStatus(), "name", user.getName()));

		map.put("message", "Google Email verified successfully");

		return map;
	}
	
	
	@Transactional
	public HashMap<String, Object> validateLoginUser(MobileVerifyRequest request)
	        throws BadRequestException {

	    HashMap<String, Object> map = new HashMap<>();

	    String mobile = Optional.ofNullable(request.getPhone_no())
	            .map(String::trim)
	            .orElse("");

	    String isdCode = Optional.ofNullable(request.getIsdcode())
	            .map(String::trim)
	            .orElse("");

	    String email = Optional.ofNullable(request.getEmail_id())
	            .map(String::trim)
	            .orElse("");

	    String identifier = !email.isBlank()
	            ? email
	            : isdCode + mobile;
	    
	    System.err.println(identifier.trim());
	    SiteUserLoginDTO user = siteUserRepo.findByMobileOrEmailAndMStatus(identifier.trim()).stream().findFirst()
				.orElseThrow(() -> new RuntimeException("User is not registered!!!!!"));
		Integer status = Optional.ofNullable(user.getMStatus()).orElse(0);
	    if (!email.isBlank()) {
	        if (status == 1) {
	            throw new BadRequestException("Registration is incomplete for email");
	        }
	        map.put("success", true);
	        map.put("message", "Validation successful");
	        return map;
	    }
	    if (status == 1) {
	        throw new BadRequestException("Registration is incomplete for email");
	    }
	    if (status == 2) {
	        throw new BadRequestException("Registration is incomplete for name");
	    }
	    if (!(status == 4 || status == 5)) {
	        throw new BadRequestException("Registration is incomplete");
	    }
	    map.put("success", true);
	    map.put("message", "Validation successful");
	    return map;
	}
	
	@Transactional
	public HashMap<String,Object> mobileLogin(MobileVerifyRequest request){

	    String mobile=request.getIsdcode()+request.getPhone_no();

	    SiteUserLoginDTO user=siteUserRepo
	            .findByMobileOrEmailAndMStatus(mobile)
	            .stream()
	            .findFirst()
	            .orElseThrow(()->new BadRequestException("Mobile no does not exist"));

	    HashMap<String,Object> map=new HashMap<>();

	    map.put("success",true);
	    map.put("message","Login with mobile successful");
	    map.put("userId",user.getUserId());
	    map.put("data",user);

	    return map;
	}
	
	@Transactional
	public HashMap<String,Object> emailLogin(MobileVerifyRequest request){

	    SiteUserLoginDTO user=siteUserRepo
	            .findByMobileOrEmailAndMStatus(request.getEmail_id())
	            .stream()
	            .findFirst()
	            .orElseThrow(()->new BadRequestException("Email does not exist"));

	    HashMap<String,Object> map=new HashMap<>();

	    map.put("success",true);
	    map.put("message","Login with email successful");
	    map.put("data",user);

	    return map;
	}
	
	@Transactional
	public HashMap<String,Object> googleLogin(MobileVerifyRequest request){

	    SiteUserLoginDTO user=siteUserRepo
	            .findByMobileOrEmailAndMStatus(request.getEmail_id())
	            .stream()
	            .findFirst()
	            .orElseThrow(()->new BadRequestException("Email does not exist"));

	    HashMap<String,Object> map=new HashMap<>();

	    map.put("success",true);
	    map.put("message","Login with Google successfully");
	    map.put("data",user);

	    return map;
	}
	
	public SiteUserLoginDTO getUserContactBySiteUserId(Integer siteUserId) {
		SiteUserLogin user = siteUserRepo.findById(siteUserId)
	            .orElseThrow(() -> new RuntimeException("Site user not found"));
	    return new SiteUserLoginDTO(user.getName(),user.getEmail());
	}
}
