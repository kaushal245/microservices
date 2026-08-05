package com.raise_ticket_service.entities;

import java.sql.Timestamp;



import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "t_issuecategory")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IssueCategoryEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "i_issuecategoryid")
	private Integer issueCategoryId;

	@Column(name = "s_name", length = 255)
	private String name;
	@JsonIgnore
	@Column(name = "ts_regdate")
	private Timestamp regDate;
	@JsonIgnore
	@Column(name = "ts_moddate")
	private Timestamp modDate;
}
