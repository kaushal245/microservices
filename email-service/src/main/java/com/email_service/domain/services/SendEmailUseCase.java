package com.email_service.domain.services;

import org.springframework.stereotype.Service;

import com.email_service.domain.model.Email;
import com.email_service.domain.port.EmailSenderPort;

@Service
public class SendEmailUseCase {

	private final EmailSenderPort emailSenderPort;

	public SendEmailUseCase(EmailSenderPort emailSenderPort) {
		this.emailSenderPort = emailSenderPort;
	}

	public void execute(Email email) {
		emailSenderPort.sendEmail(email);
	}
}
