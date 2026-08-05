package com.email_service.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.email_service.dto.MailRequestDTO;
import com.email_service.entities.SmtpEntity;
import com.email_service.impl.SendMail;
import com.email_service.reposatory.SmtpRepo;
import com.email_service.services.MailService;


@RestController
@RequestMapping("/api/mail")
public class MailController implements SendMail {
	
	
	 @Autowired
	    private MailService mailService;
	 
	 	@Autowired
	 	private SmtpRepo smtpRepo;
		@PostMapping("/send")
		public ResponseEntity<?> sendMail(@RequestBody MailRequestDTO request) {
			SmtpEntity smtp = smtpRepo.findLatestSmtpDetails();
			request.setSmtp(smtp);
			int status = mailService.postMailAttach(request);
			Map<String, Object> response = new HashMap<>();
			if (status == 1) {
				response.put("success", true);
				response.put("message", "Mail sent successfully");
				return ResponseEntity.ok(response);
			}
			response.put("success", false);
			response.put("message", "Failed to send mail");

			return ResponseEntity.badRequest().body(response);
		}
}
