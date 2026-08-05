package com.blog_service.blog_service.entity;

import java.sql.Timestamp;


import com.fasterxml.jackson.annotation.JsonFormat;

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
@Table(name = "t_blog")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BlogEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "i_blogid")
	private Integer blogId;

	@Column(name = "s_title")
	private String title;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Column(name = "d_date")
	private Timestamp date;

	@Column(name = "s_shortdesc")
	private String shortDesc;

	@Column(name = "s_keywords")
	private String keywords;

	@Column(name = "i_readtime")
	private Integer readTime;

	@Column(name = "s_teamname")
	private String teamName;

	@Column(name = "i_blogcategoryid")
	private Integer blogCategoryId;

	@Column(name = "s_image")
	private String image;

	@Column(name = "s_description")
	private String description;

	@Column(name = "i_status")
	private Integer status;

	@Column(name = "i_approval")
	private Integer approval;

	@Column(name = "i_userid")
	private Integer userId;

	@Column(name = "ts_regdate")
	private Timestamp regDate;

	@Column(name = "ts_moddate")
	private Timestamp modDate;

	@Column(name = "i_trendingflag")
	private Integer trendingFlag;

	@Column(name = "i_editorflag")
	private Integer editorFlag;

	@Column(name = "i_counter")
	private Integer counter;

	@Column(name = "s_imgcaption")
	private String imgCaption;

	@Column(name = "i_publishflagid")
	private Integer publishFlagId;

	@Column(name = "l_metatag")
	private String metaTag;

	@Column(name = "i_applicablefor")
	private Integer applicablefor;

}