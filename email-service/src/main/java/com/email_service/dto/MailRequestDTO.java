package com.email_service.dto;

import com.email_service.entities.SmtpEntity;

import lombok.Data;

@Data
public class MailRequestDTO {
	 private String[] to;
	    private String[] cc;
	    private String[] bcc;

	    private String message;
	    private String subject;

	    private String filePath;
	    private String localFileName;

	    private Integer check;

	    private String from;

	    private SmtpEntity smtp;
}
