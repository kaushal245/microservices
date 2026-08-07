package com.email_service.domain.port;

import com.email_service.domain.model.Email;

public interface EmailSenderPort {
	  int sendEmail(Email email);
}
