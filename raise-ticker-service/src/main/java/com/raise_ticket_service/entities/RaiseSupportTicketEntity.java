package com.webelement.apiuserprospur.entity;

import java.sql.Timestamp;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_raisesupportticket")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RaiseSupportTicketEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "i_raisesupportid")
	private Integer raiseSupportId;

	@Column(name = "s_issuecategory")
	private String issueCategory;

	@Column(name = "s_subject")
	private String subject;

	@Lob
	@Column(name = "s_description", columnDefinition = "LONGTEXT")
	private String description;

	@Column(name = "s_remarks")
	private String remarks;

	@Column(name = "i_status")
	private Integer status;

	@Column(name = "ts_regdate")
	private Timestamp regDate;

	@Column(name = "ts_moddate")
	private Timestamp modDate;

	@Column(name = "i_userid")
	private Integer userId;
}
