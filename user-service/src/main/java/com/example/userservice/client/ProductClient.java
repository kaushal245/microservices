package com.example.userservice.client;

import com.example.userservice.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// "product-service" here is the spring.application.name registered in Eureka
// by the product-service module. Feign + the LoadBalancer resolve it to a real
// host:port at runtime — no hardcoded URL needed.
@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ProductDTO getProductById(@PathVariable("id") Long id);
}
