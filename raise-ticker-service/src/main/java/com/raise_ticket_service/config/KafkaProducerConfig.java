package com.raise_ticket_service.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaProducerConfig {
	  @Bean
	    public ProducerFactory<String, Object> producerFactory() {

	        Map<String, Object> config = new HashMap<>();

	        config.put(
	                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
	                "localhost:9092");

//	        config.put(
//	                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
//	                StringSerializer.class);
	        config.put(
	                "spring.json.add.type.headers",
	                false);
//	        config.put(
//	                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
//	                JsonSerializer.class);
	        System.err.println("Publishing Ticket Event");
	        return new DefaultKafkaProducerFactory<>(
	        		config,
	                new StringSerializer(),
	                new JacksonJsonSerializer<>());
	    }

	    @Bean
	    public KafkaTemplate<String, Object> kafkaTemplate() {
	        return new KafkaTemplate<>(producerFactory());
	    }
}
