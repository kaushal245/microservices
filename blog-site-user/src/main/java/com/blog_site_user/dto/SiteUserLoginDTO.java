package com.blog_site_user.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@ToString
public class SiteUserLoginDTO {
	private Integer userId;
	
	private String name;
	private String mobileNo;
	private String email;
	private String pan;
	private Date dob;
	private Short status;
	private Integer mStatus;
	public SiteUserLoginDTO(String mobileNo, String email) {
		super();
		this.mobileNo = mobileNo;
		this.email = email;
	}
	
	
	

}
