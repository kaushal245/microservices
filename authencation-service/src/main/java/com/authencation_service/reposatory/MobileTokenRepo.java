package com.authencation_service.reposatory;

import java.security.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.authencation_service.entity.MobileToken;



@Repository
public interface MobileTokenRepo extends JpaRepository<MobileToken, Integer> {
	@Query("SELECT m FROM MobileToken m WHERE m.mobileNo = :mobileNo AND m.token = :token AND m.status = 1")
	Optional<MobileToken> findExpiryByMobileNoAndToken(@Param("mobileNo") String mobileNo,@Param("token") String token);

	@Query("SELECT m FROM MobileToken m WHERE m.token = :token AND m.status =1")
	Optional<MobileToken> findExpiryByToken(@Param("token") String token);

	List<MobileToken> findByMobileNo(String mobileNo);

	@Modifying
	@Transactional
	@Query("UPDATE MobileToken l " + "SET l.status = 2 " + "WHERE l.mobileNo = :mobileNo "
			+ "AND l.token = :token AND l.status =1")
	int updateTokenStatus(@Param("mobileNo") String mobileNo, @Param("token") String token);

	@Modifying
	@Transactional
	@Query("UPDATE MobileToken l " + "SET l.status = 2 " + "WHERE l.token = :token AND l.status =1")
	int updateToken(@Param("token") String token);

	Optional<MobileToken> findTopByMobileNoOrderByTokenIdDesc(String mobileNo);

}
