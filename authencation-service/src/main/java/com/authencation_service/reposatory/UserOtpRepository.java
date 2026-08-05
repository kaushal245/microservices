package com.authencation_service.reposatory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.authencation_service.entity.UserOtp;


public interface UserOtpRepository extends JpaRepository<UserOtp, Integer> {

	Optional<UserOtp> findTopByIdentifierOrderByCreatedAtDesc(String identifier);
	Optional<UserOtp> findTopByIdentifierAndOtpOrderByCreatedAtDesc(
	        String identifier,
	        String otp
	);
	@Query(
		    value = "SELECT * " +
		            "FROM user_otp " +
		            "WHERE s_identifier = :identifier " +
		            "AND s_otp = :otp " +
		            "AND ts_expires_at > NOW() " +
		            "AND s_verified = 'N' " +
		            "ORDER BY ts_created_at DESC ",
		    nativeQuery = true
		)
		Optional<UserOtp> verifyOtp(@Param("identifier") String identifier,@Param("otp") String otp);
}
