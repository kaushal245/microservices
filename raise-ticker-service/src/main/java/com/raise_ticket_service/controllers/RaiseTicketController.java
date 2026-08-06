package com.raise_ticket_service.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.raise_ticket_service.client.SIteUserLoginClient;
import com.raise_ticket_service.dtos.RaiseSupportTicketDTO;
import com.raise_ticket_service.entities.IssueCategoryEntity;
import com.raise_ticket_service.helpers.Helpers;
import com.raise_ticket_service.reposatory.IssueCategoryRepo;
import com.raise_ticket_service.services.RaiseTicketService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/support")
public class RaiseTicketController {
	@Autowired
	private RaiseTicketService service;

	private Helpers commonMethod;

	@Autowired
	private IssueCategoryRepo issueCategoryRepo;
	
	@Autowired
	private SIteUserLoginClient siteUserClient;

	@PostMapping("/save_raised_ticked")
	public ResponseEntity<?> saveOrUpdate(@RequestBody RaiseSupportTicketDTO request, HttpServletRequest httpRequest,
			@RequestHeader(value = "token", required = true) String token) throws BadRequestException {
		String apiToken = Optional.ofNullable(token).map(String::trim).filter(s -> !s.isBlank())
				.orElseThrow(() -> new BadRequestException("Token is required"));

		Map<String, String> errs = new HashMap<>();
		if (request.getDescription() == null || request.getDescription().isBlank()) {
			errs.put("Description", "Description is required");
		}
		if (request.getIssueCategory() == null || request.getIssueCategory().isBlank()) {
			errs.put("IssueCategory", "Issue for category is required");
		}
		if (request.getSubject()==null || request.getSubject().isBlank()) {
			errs.put("Subject", "Subject is required");
		}

		HashMap<String, Object> userResponse = siteUserClient.getUserContactBySiteUserId(request.getSiteUserId());

		HashMap<String, Object> data = (HashMap<String, Object>) userResponse;
		String.valueOf(data.get("email"));

		String email = String.valueOf(data.get("email"));
		String name = String.valueOf(data.get("name"));

		HashMap<String, Object> response = service.saveOrUpdateTicket(request);
		if (email != null && !email.isEmpty()) {
			Map<String, Object> parameter = new HashMap<>();
			parameter.put("email", email);
			parameter.put("name", name);
			parameter.put("subject", request.getSubject());
			if (response.containsKey("ticketId")) {
				parameter.put("ticketId", response.get("ticketId"));
			}
			// this.sendTicketStatusEmail(parameter, httpRequest);
		}
		Boolean status = (Boolean) response.get("success");
		if (Boolean.TRUE.equals(status)) {
			response.remove("ticketId");
			return ResponseEntity.ok(response);
		}

		return ResponseEntity.badRequest().body(response);
	}
	@GetMapping("/issue_category_list")
	public ResponseEntity<?> getBlogSiteUserId() throws Exception {
		List<IssueCategoryEntity> issueCategoryList = issueCategoryRepo.issueCategoryList();
		Map<String, Object> response = new HashMap<>();
		if (issueCategoryList.isEmpty()) {
			response.put("success", false);
			response.put("message", "Issue category is empty");
			return ResponseEntity.ok(response);
		}
		response.put("success", true);
		response.put("message", "Issue category fetched successfully");
		response.put("data", issueCategoryList);
		return ResponseEntity.ok(response);
	}
}
