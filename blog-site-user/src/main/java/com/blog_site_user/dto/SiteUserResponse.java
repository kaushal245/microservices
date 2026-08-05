package com.blog_site_user.dto;

import lombok.Data;

@Data
public class SiteUserResponse {
	 private Integer site_user_id;
	    private String name;
	    private String email;
	    private String mobileNo;
	    private Integer mStatus;
}
