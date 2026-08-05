package com.webelement.apiuserprospur.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webelement.apiuserprospur.entity.MailLogEntity;

public interface MailLogRepo extends JpaRepository<MailLogEntity, Integer> {

}
