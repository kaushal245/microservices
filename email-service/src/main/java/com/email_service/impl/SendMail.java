package com.email_service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.email_service.dto.MailRequestDTO;

public interface SendMail {
	
	public ResponseEntity<?> sendMail(@RequestBody MailRequestDTO request);
}
