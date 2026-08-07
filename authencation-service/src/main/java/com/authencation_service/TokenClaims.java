package com.authencation_service;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final  class TokenClaims {
	private final String subject;
    private final Map<String, Object> claims;
    private final long expirationMillis;
    private final boolean refreshToken;
}
