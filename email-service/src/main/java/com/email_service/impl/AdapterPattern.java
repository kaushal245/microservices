package com.email_service.impl;

import com.email_service.dto.MailRequestDTO;

public interface AdapterPattern {
	void send(MailRequestDTO request);
}
