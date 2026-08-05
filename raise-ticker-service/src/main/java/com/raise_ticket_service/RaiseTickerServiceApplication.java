package com.raise_ticket_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class RaiseTickerServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(RaiseTickerServiceApplication.class, args);
	}
}
