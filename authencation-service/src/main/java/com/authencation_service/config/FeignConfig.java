package com.authencation_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.codec.ErrorDecoder;

@Configuration
public class FeignConfig {
	@Bean
	public ErrorDecoder errorDecoder() {
		return new SiteUserFeignErrorDecoder();
	}
}
