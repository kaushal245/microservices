package com.raise_ticket_service.dtos;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RaiseSupportTicketDTO {

	private Integer raiseSupportId;
	private String issueCategory;
	private String subject;
	private String description;
	private String remarks;
	private Integer status;
	private Timestamp regDate;
	private Timestamp modDate;
	private Integer userId;
	private Integer siteUserId;

	private String token;
	private String emailId;

	public RaiseSupportTicketDTO(Integer raiseSupportId, String issueCategory, String subject, String description,
			String remarks, Integer status, Timestamp regDate, Timestamp modDate, Integer userId, Integer siteUserId) {
		super();
		this.raiseSupportId = raiseSupportId;
		this.issueCategory = issueCategory;
		this.subject = subject;
		this.description = description;
		this.remarks = remarks;
		this.status = status;
		this.regDate = regDate;
		this.modDate = modDate;
		this.userId = userId;
		this.siteUserId = siteUserId;
	}

}
