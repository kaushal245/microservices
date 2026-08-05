package com.blog_site_user.reposatory;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blog_site_user.dto.SiteUserLoginDTO;
import com.blog_site_user.entity.SiteUserLogin;

import jakarta.transaction.Transactional;

public interface SiteUserLoginReco extends JpaRepository<SiteUserLogin, Integer> {

	Optional<SiteUserLogin> findByEmail(String email);

	boolean existsByEmail(String email);

	boolean existsByMobileNo(String mobileNo);

	Optional<SiteUserLogin> findByMobileNoOrEmail(String mobileNo, String email);

	@Query("SELECT s FROM SiteUserLogin s " + "WHERE (s.mobileNo = :identifier OR s.email = :identifier) "
			+ "AND s.status = 1")
	Optional<SiteUserLogin> findByMobileOrEmailAndStatus(@Param("identifier") String identifier);

	@Query("SELECT s FROM SiteUserLogin s " + "WHERE (s.mobileNo = :identifier OR s.email = :identifier) "
			+ "AND s.status = 1")
	List<SiteUserLogin> findByMobileOrEmailAndStatus1(@Param("identifier") String identifier);
	
	

	Optional<SiteUserLogin> findByUserId(Integer userId);

//	@Query("SELECT s FROM SiteUserLogin s " + "WHERE (s.mobileNo = :identifier OR s.email = :identifier) "
//			+ "AND s.mStatus IN (4,5)")
//	List<SiteUserLogin> findByMobileOrEmailAndMStatus(@Param("identifier") String identifier);

	@Query("SELECT s FROM SiteUserLogin s " + "WHERE (s.userId = :siteUserId) " + "AND s.status = 1")
	List<SiteUserLogin> findBySiteUserId(@Param("siteUserId") Integer siteUserId);

	@Query("SELECT s FROM SiteUserLogin s " + "WHERE (s.mobileNo = :mobileNo) " + "AND s.status = 1")
	List<SiteUserLogin> findBySiteMobileNo(@Param("mobileNo") String mobileNo);

	@Query("SELECT new com.blog_site_user.dto.SiteUserLoginDTO("
			+ "s.userId, COALESCE(s.name, ''),COALESCE(s.mobileNo,''), COALESCE(s.email,''),COALESCE(s.pan,''),s.dob,s.status, s.mStatus) " + "FROM SiteUserLogin s "
			+ "WHERE (s.mobileNo = :identifier OR s.email = :identifier) " + "AND s.mStatus IN (4,5)")
	List<SiteUserLoginDTO> findByMobileOrEmailAndMStatus(@Param("identifier") String identifier);

	@Query("SELECT new com.blog_site_user.dto.SiteUserLoginDTO("
			+ "s.userId, s.name,s.mobileNo, s.email , s.pan, s.dob, s.status, s.mStatus) " + "FROM SiteUserLogin s "
			+ "WHERE (s.mobileNo = :identifier OR s.email = :identifier) " + "AND s.status = 1")
	List<SiteUserLoginDTO> findByMobileOrEmailAndStatus2(@Param("identifier") String identifier);

	@Query("SELECT s FROM SiteUserLogin s " + "WHERE (s.userId = :siteUserId) " + "AND s.status = 1")
	Optional<SiteUserLogin> findByUserId(@Param("siteUserId") String siteUserId);
	
	
	@Modifying
	@Transactional
	@Query("UPDATE SiteUserLogin s " +
	       "SET s.fcmToken = :fcmToken " +
	       "WHERE s.mobileNo = :mobileNo ")
	int updateFcmToken(@Param("mobileNo") String mobileNo,
	                   @Param("fcmToken") String fcmToken);
	
	
	
	

}