package com.email_service.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.email_service.dto.MailRequestDTO;
import com.email_service.entities.SmtpEntity;
import com.email_service.helpers.Helpers;
import com.email_service.impl.SendMail;
import com.email_service.reposatory.SmtpRepo;
import com.email_service.services.MailService;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/mail")
public class MailController implements SendMail {
	
	
	 	@Autowired
	    private MailService mailService;
	 	
	 	@Autowired
	 	private Helpers helpers;
	 
	 	@Autowired
	 	private SmtpRepo smtpRepo;
	 	
	 	@Value("${file_maillog:}")
		private String file_maillog;
		@PostMapping("/send")
		public ResponseEntity<?> sendMail(@RequestBody MailRequestDTO request) {
			SmtpEntity smtp = smtpRepo.findLatestSmtpDetails();
			request.setSmtp(smtp);
			int status = mailService.postMailAttach(request);
			Map<String, Object> response = new HashMap<>();
			if (status == 1) {
				response.put("success", true);
				response.put("message", "Mail sent successfully");
				return ResponseEntity.ok(response);
			}
			response.put("success", false);
			response.put("message", "Failed to send mail");

			return ResponseEntity.badRequest().body(response);
		}
		
		
//		public ResponseEntity<HashMap<String, Object>> sendTicketStatusEmail(@RequestBody MailRequestDTO request) {
//
//			String userEmail = request.getEmail();
//
//			if (userEmail == null || userEmail.isBlank()) {
//				throw new RuntimeException("Email is required");
//			}
//
//			String name = request.getName();
//			String ticketSubject = request.getTicketSubject();
//			Integer ticketId = request.getTicketId();
//
//			String subject = "Ticket submitted: {{" + ticketId + "}}";
//
//			String mailBody = helpers.getTicketRaiseCreate(name, ticketSubject, ticketId);
//
//			SmtpEntity smtpDetail = smtpRepo.findLatestSmtpDetails();
//
//			if (smtpDetail == null) {
//				throw new RuntimeException("SMTP details not found");
//			}
//
//			String filePath = helpers.createFolder(file_maillog);
//
//			String fileName = helpers.writeHTMLFile(mailBody, file_maillog + "/" + filePath,
//					"np-" + System.currentTimeMillis());
//
//			MailRequestDTO mailRequest = new MailRequestDTO();
//
//			mailRequest.setTo(new String[] { userEmail });
//			mailRequest.setCc(new String[] { "" });
//			mailRequest.setBcc(new String[] {});
//			mailRequest.setSubject(subject);
//			mailRequest.setMessage(mailBody);
//			mailRequest.setAttachmentPath("");
//			mailRequest.setAttachmentName("");
//			mailRequest.setUserId(-1);
//			mailRequest.setRemark("");
//			mailRequest.setSmtp(smtpDetail);
//
//			int status = mailService.postMailAttach(mailRequest);
//
//			if (status <= 0) {
//				throw new RuntimeException("Failed to send email");
//			}
//
//			String ipLocal = helpers.getLocalIp();
//
//			helpers.createMailLog(3, name, userEmail, "", "", smtpDetail.getFrom(), subject, filePath + "/" + fileName,
//					"", ipLocal, 1);
//
//			HashMap<String, Object> response = new HashMap<>();
//			response.put("success", true);
//			response.put("message", "Email sent successfully");
//			return ResponseEntity.ok(response);
//		}

		public void sendTicketStatusEmail(MailRequestDTO request) {

			String userEmail = request.getEmail();
			System.err.println("Send Mail " + userEmail);
			String name = request.getName();
			String ticketSubject = request.getTicketSubject();
			Integer ticketId = request.getTicketId();

			if (userEmail == null || userEmail.isBlank()) {
				throw new RuntimeException("Email is required");
			}

			String subject = "Ticket submitted: {{" + ticketId + "}}";

			String mailBody = helpers.getTicketRaiseCreate(name, ticketSubject, ticketId);

			SmtpEntity smtpDetail = smtpRepo.findLatestSmtpDetails();

			String[] to = { userEmail };
			String filePath = helpers.createFolder(file_maillog);
			String fileName = helpers.writeHTMLFile(mailBody, file_maillog + "/" + filePath,
					"np-" + System.currentTimeMillis());

			MailRequestDTO mailRequest = MailRequestDTO.builder()
			        .to(to)
			        .cc(new String[]{})
			        .bcc(new String[]{})
			        .subject(subject)
			        .message(mailBody)
			        .AttachmentPath("")
			        .AttachmentName("")
			        .UserId(-1)
			        .Remark("")
			        .smtp(smtpDetail)
			        .build();
			int status = mailService.postMailAttach(mailRequest);

			String ipLocal = helpers.getLocalIp();

			if (status <= 0) {
				throw new RuntimeException("Failed to send email");
			}

			helpers.createMailLog(3, name, userEmail, "", "", smtpDetail.getFrom(), subject, filePath + "/" + fileName,
					"", ipLocal, 1);

		}


	
}
