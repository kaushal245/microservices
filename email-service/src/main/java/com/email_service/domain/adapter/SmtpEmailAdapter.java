package com.email_service.domain.adapter;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import org.springframework.stereotype.Component;

import com.email_service.domain.model.Email;
import com.email_service.domain.port.EmailSenderPort;
import com.email_service.entities.SmtpEntity;

import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

@Component
public class SmtpEmailAdapter  implements EmailSenderPort{

	@Override
	public int sendEmail(Email request) {
		try {

			if (request.getSmtp() == null) {
				throw new IllegalStateException("No SMTP configuration found.");
			}

			SmtpEntity smtp = request.getSmtp();

			// SMTP Properties
			Properties properties = new Properties();
			properties.put("mail.smtp.host", smtp.getHost());

			if (smtp.getPort() != null && !smtp.getPort().isBlank()) {
				properties.put("mail.smtp.port", smtp.getPort());
			}
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			properties.put("mail.smtp.socketFactory.fallback", "false");
			properties.put("mail.smtp.starttls.enable", "true");
			properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
			// Authentication
			Session session = Session.getInstance(properties, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(smtp.getServerUsername(), smtp.getServerPassword());
				}
			});
			MimeMessage mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress(smtp.getFrom(), smtp.getDisplayName()));
			mimeMessage.setSubject(request.getSubject());
			if (request.getCheck() != null && request.getCheck() > 0) {
				mimeMessage.addHeader("Disposition-Notification-To", smtp.getFrom());
			}
			// TO
			if (request.getTo() != null && request.getTo().length > 0) {
				InternetAddress[] toAddresses = new InternetAddress[request.getTo().length];
				for (int i = 0; i < request.getTo().length; i++) {
					toAddresses[i] = new InternetAddress(request.getTo()[i]);
				}
				mimeMessage.setRecipients(Message.RecipientType.TO, toAddresses);
			}
			// CC
			if (request.getCc() != null && request.getCc().length > 0) {
				InternetAddress[] ccAddresses = new InternetAddress[request.getCc().length];
				for (int i = 0; i < request.getCc().length; i++) {
					ccAddresses[i] = new InternetAddress(request.getCc()[i]);
				}
				mimeMessage.setRecipients(Message.RecipientType.CC, ccAddresses);
			}
			// BCC
			if (request.getBcc() != null && request.getBcc().length > 0) {
				InternetAddress[] bccAddresses = new InternetAddress[request.getBcc().length];
				for (int i = 0; i < request.getBcc().length; i++) {
					bccAddresses[i] = new InternetAddress(request.getBcc()[i]);
				}
				mimeMessage.setRecipients(Message.RecipientType.BCC, bccAddresses);
			}
			// Email Body
			MimeBodyPart bodyPart = new MimeBodyPart();
			bodyPart.setContent(request.getMessage(), "text/html; charset=UTF-8");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(bodyPart);
			// Attachments
			if (request.getFilePath() != null && !request.getFilePath().isBlank()) {
				if (request.getLocalFileName() != null && request.getLocalFileName().contains(",")) {
					String[] fileNames = request.getLocalFileName().split(",");
					String[] filePaths = request.getFilePath().split(",");
					for (int i = 0; i < fileNames.length; i++) {
						File file = new File(filePaths[i].trim());
						if (file.exists()) {
							MimeBodyPart attachment = new MimeBodyPart();
							attachment.setDataHandler(new DataHandler(new FileDataSource(file)));
							attachment.setFileName(fileNames[i].trim());
							multipart.addBodyPart(attachment);
						}
					}
				} else {
					File file = new File(request.getFilePath());
					if (file.exists()) {
						MimeBodyPart attachment = new MimeBodyPart();
						attachment.setDataHandler(new DataHandler(new FileDataSource(file)));
						attachment.setFileName(request.getLocalFileName());
						multipart.addBodyPart(attachment);
					}
				}
			}
			mimeMessage.setContent(multipart);
			mimeMessage.setSentDate(new Date());
			Transport.send(mimeMessage);
			return 1;

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

}
