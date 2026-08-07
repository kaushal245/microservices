package com.authencation_service.factory;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Value;

import com.authencation_service.OtpPayload;
import com.authencation_service.immutable.OtpType;

import Strategy.OtpGenerator;

public class SmsOtpGenerator implements OtpGenerator {

	  private static final SecureRandom RANDOM = new SecureRandom();
	    private final int expiryMinutes;

	    public SmsOtpGenerator(@Value("${OTPExpireTime}") int expiryMinutes) {
	        this.expiryMinutes = Math.min(expiryMinutes, 3);
	    }

	    @Override
	    public OtpType supports() { return OtpType.SMS; }

	    @Override
	    public OtpPayload generate(String recipient) {
	        String code = String.format("%04d", RANDOM.nextInt(10_000));
	        return OtpPayload.builder()
	                .code(code).recipient(recipient).type(OtpType.SMS)
	                .expiryMinutes(expiryMinutes).build();
	    }

}
