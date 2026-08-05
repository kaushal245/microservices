package com.authencation_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication

@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.authencation_service.client")
public class AuthencationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthencationServiceApplication.class, args);
	}

}
