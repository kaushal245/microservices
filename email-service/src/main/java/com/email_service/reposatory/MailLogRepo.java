package com.email_service.reposatory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.email_service.entities.MailLogEntity;
@Repository
public interface MailLogRepo extends JpaRepository<MailLogEntity, Integer> {

}
