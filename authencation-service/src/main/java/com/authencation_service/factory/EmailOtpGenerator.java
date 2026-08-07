package com.authencation_service.factory;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Value;

import com.authencation_service.OtpPayload;
import com.authencation_service.immutable.OtpType;

import Strategy.OtpGenerator;

public class EmailOtpGenerator implements OtpGenerator {

	private static final SecureRandom RANDOM = new SecureRandom();
    private final int expiryMinutes;
    private final String templatePath;

    public EmailOtpGenerator(@Value("${OTPExpireTime}") int expiryMinutes,
                              @Value("${email.template.contactus}") String templatePath) {
        this.expiryMinutes = expiryMinutes;
        this.templatePath = templatePath;
    }

    @Override
    public OtpType supports() { return OtpType.EMAIL; }

    @Override
    public OtpPayload generate(String recipient) {
        String code = String.format("%06d", RANDOM.nextInt(1_000_000));
        return OtpPayload.builder()
                .code(code).recipient(recipient).type(OtpType.EMAIL)
                .expiryMinutes(expiryMinutes).templatePath(templatePath)
                .build();
    }

}
