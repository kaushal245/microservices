package com.email_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.email_service.controllers.MailController;
import com.email_service.domain.model.Email;
import com.email_service.domain.services.SendEmailUseCase;
import com.email_service.dto.MailRequestDTO;
import com.email_service.dto.TicketCreatedEvent;
import com.email_service.services.MailService;

import jakarta.mail.internet.InternetAddress;
import tools.jackson.databind.ObjectMapper;

@Component
public class TicketEmailConsumer {

	  private final SendEmailUseCase sendEmailUseCase;

	    public TicketEmailConsumer(
	            SendEmailUseCase sendEmailUseCase) {
	        this.sendEmailUseCase =
	                sendEmailUseCase;
	    }
	
//	@KafkaListener(
//	        topics = "ticket-created-topic-v2",
//	        groupId = "mail-group")
//	public void consume(String email) {
//
//	    try {
//	        System.out.println("Received Ticket Event");
//	        System.out.println("Email = [" + email + "]");
//
//	        // Validate email
//	        if (email == null || email.trim().isEmpty()) {
//	            throw new RuntimeException("Email is empty");
//	        }
//
//	        email = email.trim().replace("\"", "");
//	        System.out.println("Email after cleanup = [" + email + "]");
//
//	        InternetAddress internetAddress = new InternetAddress(email);
//	        internetAddress.validate();
//
//	        MailRequestDTO request = new MailRequestDTO();
//	        request.setEmail(email);
//
//	        mailService.sendTicketStatusEmail(request);
//
//	        System.out.println("Email sent successfully");
//
//	    } catch (Exception e) {
//	        System.err.println("Failed to send email to: " + email);
//	        e.printStackTrace();
//	    }
//	}
	
	@KafkaListener(
		    topics = "ticket-created-topic-v2",
		    groupId = "mail-group-v2",
		    containerFactory = "kafkaListenerContainerFactory"
		)
		public void consume(TicketCreatedEvent event) {
		    try {
		    	
		        System.out.println(new ObjectMapper().writeValueAsString(event));
		        System.out.println("Received Event: " + event);
		        String email = event.getEmail();
		        // Validate email
		        if (email == null || email.trim().isEmpty()) {
		            throw new RuntimeException("Email is empty");
		        }
		        email = email.trim().replace("\"", "");
		        InternetAddress internetAddress = new InternetAddress(email);
		        internetAddress.validate();
		        Email request = Email.builder()
		                .email(email)
		                .name(event.getName())
		                .ticketId(event.getTicketId())
		                .subject(event.getSubject())
		                .build();
		        sendEmailUseCase.execute(request);
		        System.out.println("Email sent successfully to: " + email);
		    } catch (Exception e) {
		        System.err.println("Failed to process TicketCreatedEvent: " + event);
		        e.printStackTrace();
		    }
		}
	
}
