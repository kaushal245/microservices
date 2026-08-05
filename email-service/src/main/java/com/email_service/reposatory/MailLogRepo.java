package com.email_service.reposatory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.email_service.entities.MailLogEntity;

public interface MailLogRepo extends JpaRepository<MailLogEntity, Integer> {

}
