package com.authencation_service.factory;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.authencation_service.OtpPayload;
import com.authencation_service.immutable.OtpType;

import Strategy.OtpGenerator;

@Component
public class OtpFactory {
	  private final Map<OtpType, OtpGenerator> generators;

	    public OtpFactory(List<OtpGenerator> generators) {
	        this.generators = generators.stream()
	                .collect(Collectors.toMap(OtpGenerator::supports, Function.identity()));
	    }

	    public OtpPayload create(String recipient, OtpType type) {
	        OtpGenerator generator = generators.get(type);
	        if (generator == null) {
	            throw new IllegalArgumentException("No generator for OTP type: " + type);
	        }
	        return generator.generate(recipient);
	    }
}
