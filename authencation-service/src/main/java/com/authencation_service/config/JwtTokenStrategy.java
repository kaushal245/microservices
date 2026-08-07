package com.authencation_service.config;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;

import com.authencation_service.TokenClaims;

import Strategy.TokenStrategy;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class JwtTokenStrategy implements TokenStrategy{
	
	 private final SecretKey key;
	 public JwtTokenStrategy(@Value("${jwt.secret}") String secret) {
	        this.key = Keys.hmacShaKeyFor(secret.getBytes());
	    }
	@Override
	public String generate(TokenClaims claims) {
		 return Jwts.builder()
	                .subject(claims.getSubject())
	                .claims(claims.getClaims())
	                .issuedAt(new Date())
	                .expiration(new Date(System.currentTimeMillis() + claims.getExpirationMillis()))
	                .signWith(key)
	                .compact();
	}

	@Override
	public boolean validate(String token) {
		try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
	}

	@Override
	public String extractSubject(String token) {
		Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        return claims.getSubject();
	}

}
