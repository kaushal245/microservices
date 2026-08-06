package com.raise_ticket_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.raise_ticket_service.dtos.TicketCreatedEvent;

@Service
public class TicketEventPublisher {
	  @Autowired
	    private KafkaTemplate<String, Object> kafkaTemplate;
	  
	  
	  public void publish(TicketCreatedEvent event) {
		  String email = "kaushal.panchal@webelementinc.com";
	        kafkaTemplate.send(
	                "ticket-created-topic-v2",
	                event);

	        System.err.println(event.getEmail());
	        System.err.println(
	                "Ticket event published");
	    }
}
