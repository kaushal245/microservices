package com.blog_service.blog_service.entity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

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

@Entity
@Table(name = "t_blog_site_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BlogSiteUserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "i_blog_site_userid")
	private Integer blogSiteUserId;

	@Column(name = "i_blogid")
	private Integer blogId;

	@Column(name = "i_site_userid")
	private Integer siteUserId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ts_regdate")
	private Date regDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ts_moddate")
	private Date modDate;
	
	@Column(name = "s_status")
	private short status;
}
