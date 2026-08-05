package com.authencation_service.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_mfstack_otp")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MFStackOtpEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "i_id")
	private Integer id;

	@Column(name = "s_identifier")
	private String identifier;

	@Column(name = "s_otp")
	private String otp;

	@Column(name = "ts_expires_at")
	private Timestamp expiresAt;

	@Column(name = "s_verified")
	private String verified;

	@Column(name = "ts_created_at")
	private Timestamp createdAt;
}
