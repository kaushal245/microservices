package com.blog_service.blog_service.entity;

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
@Table(name = "t_blogcategory")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogCategoryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "i_blogcategoryid")
	private Integer blogCategoryId;

	@Column(name = "s_blogcategoryname")
	private String blogCategoryName;

	@Column(name = "i_userid")
	private Integer userId;

	@Column(name = "i_status")
	private Integer status;

	@Column(name = "ts_regdate")
	private Timestamp regDate;

	@Column(name = "ts_moddate")
	private Timestamp modDate;
}