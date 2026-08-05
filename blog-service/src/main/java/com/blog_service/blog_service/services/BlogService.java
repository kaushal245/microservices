package com.blog_service.blog_service.services;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blog_service.blog_service.client.AuthServiceFeingClient;
import com.blog_service.blog_service.dto.BlogCategoryDTO;
import com.blog_service.blog_service.dto.BlogDTO;
import com.blog_service.blog_service.dto.TokenValidationRequest;
import com.blog_service.blog_service.reposatory.BlogRepo;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;


@Service
public class BlogService {

	
	
	
	@Autowired
	private BlogRepo blogRepo;
	
	 

	public Page<BlogDTO> getBlogForSearch(int page, int size, String search, Integer blogCategoryId,
			Integer minReadTime, Integer maxReadTime, LocalDate startDate, LocalDate endDate) {
		Timestamp startTimestamp = null;
		Timestamp endTimestamp = null;
		if (startDate != null) {
			startTimestamp = Timestamp.valueOf(startDate.atStartOfDay());
		}
		if (endDate != null) {
			endTimestamp = Timestamp.valueOf(endDate.atTime(23, 59, 59));
		}
		Pageable pageable = PageRequest.of(page, size, Sort.by("blogId").descending());
		return blogRepo.getBlogForSearch(pageable, search, blogCategoryId, minReadTime, maxReadTime, startTimestamp,
				endTimestamp);
	}

	public BlogDTO getBlogById(Integer id) {
		BlogDTO dto = blogRepo.getBlogById(id);
		if (dto == null) {
			throw new RuntimeException("Blog detail not found!!");
		}
		blogRepo.incrementCounter(id);
		return dto;
	}

	public List<BlogCategoryDTO> BlogCategoryService() {
		return blogRepo.findAllActiveBlogCategoryIdAndName();
	}
}
