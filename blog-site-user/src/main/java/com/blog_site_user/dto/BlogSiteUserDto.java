package com.blog_site_user.dto;

import java.util.Date;
import java.util.LinkedList;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BlogSiteUserDto {
	private String token;
	private String siteUserId;
	private String emailId;
	private String blogId;
	private String paltform;
	private String fcm_token;	
//	private LinkedList<String blogIds;
}
