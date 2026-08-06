package com.email_service.config;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.email_service.dto.TicketCreatedEvent;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {
//	@Bean
//	public ConsumerFactory<String, String> consumerFactory() {
//
//		System.err.println("Checck  >>>>>>>>>>>>>>>>>");
//	    Map<String, Object> props =
//	            new HashMap<>();
//
//	    props.put(
//	            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
//	            "localhost:9092");
//
//	    props.put(
//	            ConsumerConfig.GROUP_ID_CONFIG,
//	            "mail-group");
//
//	    props.put(
//	            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
//	            StringDeserializer.class);
//
//	    props.put(
//	            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
//	            StringDeserializer.class);
//	    System.err.println("Checck end  >>>>>>>>>>>>>>>>>");
//	    return new DefaultKafkaConsumerFactory<>(props);
//	}
//
//	@Bean
//	public ConcurrentKafkaListenerContainerFactory<String, String>
//	kafkaListenerContainerFactory() {
//
//	    ConcurrentKafkaListenerContainerFactory<String, String> factory =
//	            new ConcurrentKafkaListenerContainerFactory<>();
//
//	    factory.setConsumerFactory(
//	            consumerFactory());
//
//	    return factory;
//	}
	
	@Bean
	public ConsumerFactory<String, TicketCreatedEvent> consumerFactory() {

	    Map<String, Object> props = new HashMap<>();

	    props.put(
	            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
	            "localhost:9092");

	    props.put(
	            ConsumerConfig.GROUP_ID_CONFIG,
	            "mail-group-v2");

	    JacksonJsonDeserializer<TicketCreatedEvent> deserializer =
	            new JacksonJsonDeserializer<>(TicketCreatedEvent.class);

	    deserializer.addTrustedPackages("*");
	    deserializer.setUseTypeHeaders(false);

	    return new DefaultKafkaConsumerFactory<>(
	            props,
	            new StringDeserializer(),
	            deserializer);
	}
	
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TicketCreatedEvent>
    kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, TicketCreatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(
                consumerFactory());

        return factory;
    }

  
}
