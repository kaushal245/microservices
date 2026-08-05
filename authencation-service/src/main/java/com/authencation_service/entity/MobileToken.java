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
@Table(name = "t_mobiletoken")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MobileToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "i_tokenid")
	private Integer tokenId;
	@Column(name = "s_mobile_no")
	private String mobileNo;

	@Column(name = "s_token", columnDefinition = "LONGTEXT")
	private String token;

	@Column(name = "i_status", columnDefinition = "INT DEFAULT 1")
	private Integer status = 1;

	@Column(name = "ts_expiry")
	private Timestamp expiry;
}
