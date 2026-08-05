package com.blog_service.blog_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.codec.ErrorDecoder;

@Configuration
public class SiteUserFeignConfig {
	  @Bean
	    public ErrorDecoder siteUserErrorDecoder() {
	        return new SiteUserFeignErrorDecoder();
	    }
}
