package com.blog_site_user.dto;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class Helpers {
	
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
	
	private void validatePhoneNumber(String headerPhoneNo, String bodyPhoneNo) {
		Stream.of(headerPhoneNo == null || headerPhoneNo.trim().isBlank(), !Objects.equals(headerPhoneNo, bodyPhoneNo))
				.filter(Boolean::booleanValue).findFirst().ifPresent(data -> {
					throw new RuntimeException("Something went wrong!!");
				});
	}

	public void validateMobileNumber(MobileVerifyRequest request) throws BadRequestException {
		System.err.println(request.getPhone_no());
		String phoneNo = Optional.ofNullable(request.getPhone_no().trim()).map(String::trim).orElse("");

		String isdCode = Optional.ofNullable(request.getIsdcode()).map(String::trim).orElse("");

		// Phone number required
		if (phoneNo.isEmpty()) {
			throw new BadRequestException("Mobile number is required");
		}

		// Numeric validation
		if (!isNumeric(phoneNo)) {
			throw new BadRequestException("Mobile no must contain only numbers");
		}

		// Country code validation
		if (isdCode.isEmpty()) {
			throw new BadRequestException("Country code is required");
		}

		// Mobile length validation
		if (phoneNo.length() != 10) {
			throw new BadRequestException("Mobile no must be 10 digit");
		}
	}

	public boolean isNumeric(String value) {

		if (value == null || value.trim().isEmpty()) {
			return false;
		}

		return value.matches("\\d+");
	}
}
