package com.authencation_service.reposatory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.authencation_service.entity.SmtpEntity;



public interface SmtpRepo extends JpaRepository<SmtpEntity, Integer> {

	@Query(value = "SELECT * FROM t_smtp ORDER BY ts_regdate DESC LIMIT 1", nativeQuery = true)
	SmtpEntity findLatestSmtpDetails();

}
