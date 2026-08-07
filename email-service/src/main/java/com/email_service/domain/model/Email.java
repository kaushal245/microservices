package com.email_service.domain.model;

import com.email_service.entities.SmtpEntity;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Email {

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
	    
	    private String AttachmentPath ;
	    private String AttachmentName;
	    private Integer UserId;
	    private String Remark;
	    
	    private String name;
	    private String email;
	    private Integer ticketId;
	    private HttpServletRequest httpRequest;
	    private String ticketSubject;
}
