package com.authencation_service;

import com.authencation_service.immutable.OtpType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class OtpPayload {
	 private final String code;
	    private final String recipient;
	    private final OtpType type;
	    private final int expiryMinutes;
	    private final String templatePath;
}
