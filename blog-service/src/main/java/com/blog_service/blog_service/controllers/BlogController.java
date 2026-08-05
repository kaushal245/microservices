package com.blog_service.blog_service.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog_service.blog_service.dto.BlogCategoryDTO;
import com.blog_service.blog_service.dto.BlogDTO;
import com.blog_service.blog_service.reposatory.BlogRepo;
import com.blog_service.blog_service.services.BlogService;



@RestController
@RequestMapping("/api/blog")
public class BlogController {
	
	
	@Autowired
	private BlogService service;

	@GetMapping("/test")
	public List<?> test(){	
		return List.of(12,33,2,2,5,5,55,55);
	}
	
	
	@GetMapping("/blog_list")
	public ResponseEntity<?> blogList(@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "categoryId", required = false) Integer categoryId,
			@RequestParam(value = "minReadTime", required = false) Integer minReadTime,
			@RequestParam(value = "maxReadTime", required = false) Integer maxReadTime,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) throws BadRequestException {
		LocalDate start = parseDate(startDate);
		LocalDate end = parseDate(endDate);
		Page<BlogDTO> blogs = service.getBlogForSearch(page, size, search.trim(), categoryId, minReadTime, maxReadTime,start, end);
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("data", blogs.getContent());
		response.put("totalElements", blogs.getTotalElements());
		response.put("pages", blogs.getTotalPages());
		response.put("currentPage", blogs.getNumber());
		response.put("size", blogs.getSize());
		response.put("search", search != null ? search : "");
		response.put("blogCategoryId", categoryId != null ? categoryId : "");
		response.put("minReadTime", minReadTime != null ? minReadTime : "");
		response.put("maxReadTime", maxReadTime != null ? maxReadTime : "");
		response.put("startDate", startDate != null ? startDate : "");
		response.put("endDate", endDate != null ? endDate : "");
		if (blogs.isEmpty()) {
			response.put("success", false);
			response.put("message", "No records found");
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping("/blog_detail/{id}")
	public ResponseEntity<?> getBlogById(@PathVariable Integer id) throws BadRequestException {

		BlogDTO blog = service.getBlogById(id);
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("data", blog);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/blog_category")
	public ResponseEntity<?> blogCategory() {
		List<BlogCategoryDTO> data = service.BlogCategoryService();
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("data", data);
		if (data.isEmpty()) {
			response.put("success", false);
			response.put("message", "No records found");
		}
		return ResponseEntity.ok(response);
	}

	private LocalDate parseDate(String date) {
		if (date == null || date.trim().isEmpty()) {
			return null;
		}
		date = date.trim().replaceAll("\\s+", "");
		try {
			return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		} catch (Exception e) {
			try {
				return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
			} catch (Exception ex) {
				throw new IllegalArgumentException("Invalid date format. Use yyyy-MM-dd or dd-MM-yyyy");
			}
		}
	}

	public String escapeHtml(String value) {
		if (value == null) {
			return "";
		}
		return value.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;")
				.replaceAll("'", "&#39;");
	}
	
	
}
