package com.raise_ticket_service.dtos;

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
