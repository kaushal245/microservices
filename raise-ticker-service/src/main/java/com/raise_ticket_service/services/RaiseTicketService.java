
package com.raise_ticket_service.services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.raise_ticket_service.dtos.RaiseSupportTicketDTO;
import com.raise_ticket_service.dtos.TicketCreatedEvent;
import com.raise_ticket_service.entities.IssueCategoryEntity;
import com.raise_ticket_service.entities.RaiseSupportTicketEntity;
import com.raise_ticket_service.reposatory.IssueCategoryRepo;
import com.raise_ticket_service.reposatory.RaiseSupportTicketRepository;


@Service
public class RaiseTicketService {

	@Autowired
	private RaiseSupportTicketRepository repository;
	
	
	@Autowired
	private IssueCategoryRepo issueCategoryRepo;
	
	@Autowired
	private TicketEventPublisher publisher;

	public HashMap<String, Object> saveOrUpdateTicket(RaiseSupportTicketDTO dto) {

		HashMap<String, Object> map = new HashMap<>();
		RaiseSupportTicketEntity ticket;

		List<IssueCategoryEntity> categories = issueCategoryRepo.findByNameContainingIgnoreCase(dto.getIssueCategory());
		if (categories.isEmpty()) {
			throw new RuntimeException("Issue category not found!!");
		}
		// UPDATE case
		if (dto.getRaiseSupportId() != null) {
			ticket = repository.findById(dto.getRaiseSupportId())
					.orElseThrow(() -> new RuntimeException("Ticket not found with ID: " + dto.getRaiseSupportId()));
			if (dto.getStatus() != null) {
				ticket.setStatus(dto.getStatus());
			}
		}
		// INSERT case
		else {
			ticket = new RaiseSupportTicketEntity();
			ticket.setStatus(1);
		}
		ticket.setIssueCategory(dto.getIssueCategory());
		ticket.setSubject(dto.getSubject());
		ticket.setDescription(dto.getDescription());
		ticket.setRemarks(dto.getRemarks());
		ticket.setRegDate(Timestamp.valueOf(LocalDateTime.now()));
		ticket.setModDate(Timestamp.valueOf(LocalDateTime.now()));
		ticket.setUserId(dto.getSiteUserId());
		repository.save(ticket);
		if (dto.getRaiseSupportId() == null) {

			TicketCreatedEvent event = new TicketCreatedEvent();
			event.setEmail(dto.getEmailId());
			event.setTicketId(ticket.getRaiseSupportId());

			event.setSubject(ticket.getSubject());

			publisher.publish(event);
		}
		map.put("ticketId", ticket.getRaiseSupportId());
		map.put("success", true);
		map.put("message", "Ticket raised successfully");
		return map;

	}
}
