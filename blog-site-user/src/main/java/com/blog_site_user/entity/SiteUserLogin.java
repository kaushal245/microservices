package com.blog_site_user.entity;



import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "t_siteuser")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SiteUserLogin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "i_userid")
	private Integer userId;

	@Column(name = "s_name")
	private String name;

	@Column(name = "s_mobileno")
	private String mobileNo;

	@Column(name = "s_email")
	private String email;

	@JsonIgnore
	@Column(name = "s_password")
	private String password;

	@Column(name = "s_pan")
	private String pan;

	@Temporal(TemporalType.DATE)
	@Column(name = "d_dob")
	private Date dob;

	@Column(name = "i_status")
	private Short status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ts_regdate")
	private Date regDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ts_moddate")
	private Date modDate;

	@Column(name = "i_transaction_alerts")
	private Integer transactionAlerts;

	@Column(name = "i_market_updates")
	private Integer marketUpdates;

	@Column(name = "i_finletter")
	private Integer finletter;

	@Column(name = "i_offers_promotions")
	private Integer offersPromotions;

	@Column(name = "i_type")
	private Integer type;

	@Column(name = " i_mstatus")
	private Integer mStatus;
	
	@Column(name = " s_fcm_token")
    private String fcmToken;

}