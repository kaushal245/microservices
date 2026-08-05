package com.blog_service.blog_service.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class BlogDTO {
	private Integer blogId;
	private String title;
	private String shortDesc;
	private String keywords;
	private Integer readTime;
	private String teamName;
	private Integer blogCategoryId;
	private String blogCategoryName;
	private String image;
	private String description;
	private Integer status;
	private Integer approval;
	private Integer userId;
	private Integer trendingFlag;
	private Integer editorFlag;
	private Integer counter;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date date;
	private String imgCaption;
	private Integer blogSiteUserId;
	private Integer applicablefor;
	private Integer siteUserId;
	private short blogSiteUserStatus;
	private Integer blogStatus;
	private Integer publishFlagId;
	
	public BlogDTO(Integer blogId, String title, String shortDesc, String keywords, Integer readTime, String teamName,
			String blogCategoryName, String image, String description, Integer status, Integer approval, Integer userId,
			Integer trendingFlag, Integer editorFlag, Integer counter, Date date, String imgCaption) {
		this.blogId = blogId;
		this.title = title;
		this.shortDesc = shortDesc;
		this.keywords = keywords;
		this.readTime = readTime;
		this.teamName = teamName;
		this.blogCategoryName = blogCategoryName;
		this.image = image;
		this.description = description;
		this.status = status;
		this.approval = approval;
		this.userId = userId;
		this.trendingFlag = trendingFlag;
		this.editorFlag = editorFlag;
		this.counter = counter;
		this.date = date;
		this.imgCaption = imgCaption;
	}

	public BlogDTO(Integer blogId, String title, String shortDesc, String keywords, Integer readTime, String teamName,
			String blogCategoryName, String image, String description, Integer status, Integer approval, Integer userId,
			Integer trendingFlag, Integer editorFlag, Integer counter, Date date, String imgCaption,
			Integer blogCategoryId) {
		super();
		this.blogId = blogId;
		this.title = title;
		this.shortDesc = shortDesc;
		this.keywords = keywords;
		this.readTime = readTime;
		this.teamName = teamName;
		this.blogCategoryName = blogCategoryName;
		this.image = image;
		this.description = description;
		this.status = status;
		this.approval = approval;
		this.userId = userId;
		this.trendingFlag = trendingFlag;
		this.editorFlag = editorFlag;
		this.counter = counter;
		this.date = date;
		this.imgCaption = imgCaption;
		this.blogCategoryId = blogCategoryId;
	}

	public BlogDTO(Integer blogId, String title, Integer readTime, Integer blogCategoryId, String blogCategoryName,
			Integer trendingFlag, Integer editorFlag, Integer counter, Date date) {
		super();
		this.blogId = blogId;
		this.title = title;
		this.readTime = readTime;
		this.blogCategoryId = blogCategoryId;
		this.blogCategoryName = blogCategoryName;
		this.trendingFlag = trendingFlag;
		this.editorFlag = editorFlag;
		this.counter = counter;
		this.date = date;
	}

	public BlogDTO(Integer blogId, String title, String shortDesc, String keywords, String blogCategoryName,
			String description) {
		super();
		this.blogId = blogId;
		this.title = title;
		this.shortDesc = shortDesc;
		this.keywords = keywords;
		this.blogCategoryName = blogCategoryName;
		this.description = description;
	}

	public BlogDTO(Integer blogId, String title, String shortDesc, String keywords, Integer readTime, String teamName,
			Integer blogCategoryId, String blogCategoryName, String description, Integer status, Integer trendingFlag,
			Date date) {
		this.blogId = blogId;
		this.title = title;
		this.shortDesc = shortDesc;
		this.keywords = keywords;
		this.readTime = readTime;
		this.teamName = teamName;
		this.blogCategoryId = blogCategoryId;
		this.blogCategoryName = blogCategoryName;
		this.description = description;
		this.status = status;
		this.trendingFlag = trendingFlag;
		this.date = date;
	}

	public BlogDTO(Integer blogSiteUserId, Integer blogId, Integer siteUserId, String title, String shortDesc,
			String keywords, Integer readTime, String teamName, Integer blogCategoryId, String blogCategoryName,
			String image, String description, Integer approval, Integer trendingFlag, Integer editorFlag,
			Integer counter, Date date, String imgCaption, Integer applicablefor,short blogSiteUserStatus,Integer blogStatus,Integer publishFlagId) {
		this.blogSiteUserId = blogSiteUserId;
		this.blogId = blogId;
		this.siteUserId = siteUserId;
		this.title = title;
		this.shortDesc = shortDesc;
		this.keywords = keywords;
		this.readTime = readTime;
		this.teamName = teamName;
		this.blogCategoryId = blogCategoryId;
		this.blogCategoryName = blogCategoryName;
		this.image = image;
		this.description = description;
		this.approval = approval;
		this.trendingFlag = trendingFlag;
		this.editorFlag = editorFlag;
		this.counter = counter;
		this.date = date;
		this.imgCaption = imgCaption;
		this.applicablefor = applicablefor;
		this.blogSiteUserStatus = blogSiteUserStatus;
		this.blogStatus = blogStatus;
		this.publishFlagId=publishFlagId;
	}
}
