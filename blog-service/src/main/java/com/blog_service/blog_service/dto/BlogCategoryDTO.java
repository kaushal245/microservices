package com.blog_service.blog_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
public class BlogCategoryDTO {

	private Integer blogCategoryId;
	private String blogCategoryName;
//	private Integer userId;
//	private Integer status;

	public BlogCategoryDTO(Integer blogCategoryId, String blogCategoryName) {
		this.blogCategoryId = blogCategoryId;
		this.blogCategoryName = blogCategoryName;
	}

}
