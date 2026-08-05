package com.authencation_service.config;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String SECRET;

	@Value("${jwt.expiration}")
	private long accessTokenExpiration;

	private SecretKey getSigningKey() {

		return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
	}

	public String generateToken(String email, Integer userId) {

		Map<String, Object> claims = new HashMap<>();

		claims.put("userId", userId);

		return Jwts.builder().claims(claims).subject(email).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + accessTokenExpiration)).signWith(getSigningKey())
				.compact();

	}

	public String generateAccessToken(String username) {

		return Jwts.builder().id(UUID.randomUUID().toString()).subject(username).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + accessTokenExpiration)).signWith(getSigningKey())
				.compact();

	}

	public String createAccessToken(String mobileNo, LocalDateTime expiryTime) {

		Date now = new Date();

		Date expiryDate = Date.from(expiryTime.atZone(ZoneId.systemDefault()).toInstant());

		return Jwts.builder()

				.id(UUID.randomUUID().toString())

				.subject(mobileNo)

				.claim("customerId", mobileNo)

				.claim("expireTimestamp", expiryDate.getTime())

				.claim("expireDateTime", expiryTime.toString())

				.issuedAt(now)

				.expiration(expiryDate)

				.signWith(getSigningKey())

				.compact();

	}

	public boolean validateToken(String token) {

		try {

			Jwts.parser()

					.verifyWith(getSigningKey())

					.build()

					.parseSignedClaims(token);

			return true;

		} catch (JwtException | IllegalArgumentException e) {

			return false;

		}

	}

	public Claims extractAllClaims(String token) {

		return Jwts.parser()

				.verifyWith(getSigningKey())

				.build()

				.parseSignedClaims(token)

				.getPayload();

	}

	public String extractUsername(String token) {

		return extractClaim(token, Claims::getSubject);

	}

	public <T> T extractClaim(String token, Function<Claims, T> resolver) {

		return resolver.apply(extractAllClaims(token));

	}

	public boolean isTokenSignatureValid(String token) {

		return validateToken(token);

	}

	public String extractTokenFromRequest(HttpServletRequest request) {

		String header = request.getHeader("Authorization");

		if (header != null && header.startsWith("Bearer ")) {

			return header.substring(7);

		}

		return null;

	}

}