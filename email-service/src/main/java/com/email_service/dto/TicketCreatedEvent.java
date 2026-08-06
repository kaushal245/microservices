package com.email_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TicketCreatedEvent {
	private Integer ticketId;
	private String email;
	private String name;
	private String subject;
}
