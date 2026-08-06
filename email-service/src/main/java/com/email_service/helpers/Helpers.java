package com.email_service.helpers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Stream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.email_service.dto.MailRequestDTO;
import com.email_service.entities.MailLogEntity;
import com.email_service.entities.SmtpEntity;
import com.email_service.reposatory.MailLogRepo;
import com.email_service.reposatory.SmtpRepo;
import com.email_service.services.MailService;

import jakarta.servlet.http.HttpServletRequest;



@Component
public class Helpers {

	
	@Value("${ipflag:}")
	private String ipFlag;
	@Autowired
	private SmtpRepo smtpRepo;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private MailLogRepo mailLogRepo;

	private int otpExpiryMinutes = 5;
	


	@Value("${email.template.contactus}")
	private String contactUsTemplatePath;
	
	private final static String ALGORITHM = "AES";
	private final static String HEX = "0123456789ABCDEF";
	private final static String secretKey = "ncfecm@1ncfecm@1";

	public static String decipher(String data) throws Exception {
		if (secretKey == null || secretKey.length() != 16)
			throw new Exception("Invalid key length - 16 bytes key needed!");
		SecretKey key = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key);
		return new String(cipher.doFinal(toByte(data)));
	}

	private static byte[] toByte(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++) {
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
		}
		return result;
	}

	public static String cipher(String data) throws Exception {
		if (secretKey == null || secretKey.length() != 16) {
			throw new Exception("Invalid key length - 16 bytes key needed!");
		}
		SecretKey key = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return toHex(cipher.doFinal(data.getBytes()));
	}

	private static String toHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}

	public void validatePhoneNumber(String headerPhoneNo, String bodyPhoneNo) {
		Stream.of(headerPhoneNo == null || headerPhoneNo.trim().isBlank(), !Objects.equals(headerPhoneNo, bodyPhoneNo))
				.filter(Boolean::booleanValue).findFirst().ifPresent(data -> {
					throw new RuntimeException("Something went wrong!!");
				});
	}
	

	public boolean isNumeric(String value) {

		if (value == null || value.trim().isEmpty()) {
			return false;
		}

		return value.matches("\\d+");
	}

	
	public boolean isEmail(String identifier) {
		return identifier != null && identifier.matches("^[A-Za-z0-9+_.-]+@(.+)$");
	}

	public boolean isValidEmail(String email) {
		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
		return email.matches(emailRegex);
	}

	

	

	public String getLoginOtpMessageCreate(String name, String otp, String valueOf) {
		try {
			Path path = Paths.get(contactUsTemplatePath, "otp.html");
			String content = Files.readString(path, StandardCharsets.UTF_8);
			content = content.replace("__NAME__", name).replace("__OTP__", otp).replace("__EXPIRY__", valueOf);
			return content;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	@SuppressWarnings("unused")
	public void createMailLog(int type, String name, String to, String cc, String bcc, String from, String subject,
			String filename, String ip, String iplocal, int status) {
		MailLogEntity log = new MailLogEntity();
		log.setType(type);
		log.setName(name);
		log.setTo(to);
		log.setCc(cc);
		log.setBcc(bcc);
		log.setFrom(from);
		log.setSubject(subject);
		log.setStatus(status);
		log.setFilename(filename);
		log.setRegDate(LocalDateTime.now());
		log.setModDate(LocalDateTime.now());
		log.setIpAddress(ip);
		log.setLocalIp(iplocal);
		mailLogRepo.save(log);
	}
	
	public String writeHTMLFile(String content, String filePath, String fileName) {
		try {
			File dir = new File(filePath);
			if (!dir.exists()) {
				dir.mkdirs(); // Use mkdirs() to ensure parent directories are also created
			}
			fileName = fileName + ".html";
			File file = new File(dir, fileName); // Cleaner path handling

			try (BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
				writer.write(content);
			}
			return fileName;
		} catch (Exception e) {
			e.printStackTrace(); // You could use a logger instead
			return null;
		}
	}

		public String getLocalIp() {
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			return localHost.getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
			return "UNKNOWN";
		}
	}

	public String resolveClientIp(HttpServletRequest request) {
		String ipAddrStr = "";
		String iplocalserver = ipFlag;

		try {
			if ("localIp".equalsIgnoreCase(iplocalserver)) {
				// Get local server IP address
				InetAddress addr = InetAddress.getLocalHost();
				ipAddrStr = addr.getHostAddress();
			} else {
				// Try to get real client IP from headers (in case of proxy or load balancer)
				ipAddrStr = request.getHeader("X-FORWARDED-FOR");

				// Fallback to remote address
				if (ipAddrStr == null || ipAddrStr.isEmpty()) {
					ipAddrStr = request.getRemoteAddr();
				}
			}
		} catch (Exception e) {
			System.out.println("Error retrieving IP: " + e.getMessage());
			ipAddrStr = "UNKNOWN";
		}
//		System.out.println("Resolved IP: " + ipAddrStr);
		return ipAddrStr;
	}
	
	public String createFolder(String path) {
		String foldername = "";
		try {
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Calcutta"));
			int month = (cal.get(Calendar.MONTH) + 1);
			int year = cal.get(Calendar.YEAR);
			SimpleDateFormat sdf1 = new SimpleDateFormat("M");
			SimpleDateFormat sdf2 = new SimpleDateFormat("MMM");
			String monthName = sdf2.format(sdf1.parse(month + ""));

			foldername = (monthName + "-" + year).toLowerCase();
			File dir = new File(path + foldername + "/");
			if (!dir.exists()) {
				dir.mkdirs(); // This is safer
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return foldername;
	}
	
	public String getTicketRaiseCreate(String name, String subjectticket, int ticketId) {
		try {

			Path path = Paths.get(contactUsTemplatePath, "Raiseticket.html");
			String content = Files.readString(path, StandardCharsets.UTF_8);
			content = content.replace("__NAME__", name).replace("__REMARK__", subjectticket).replace("__TICKET_NO__",
					String.valueOf(ticketId));

			return content;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
}
