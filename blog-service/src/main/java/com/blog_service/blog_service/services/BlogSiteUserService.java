package com.blog_service.blog_service.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog_service.blog_service.client.SiteUserFeingClient;
import com.blog_service.blog_service.config.SiteUserFeignConfig;
import com.blog_service.blog_service.dto.BlogDTO;
import com.blog_service.blog_service.dto.BlogSiteUserDto;
import com.blog_service.blog_service.dto.TokenValidationRequest;
import com.blog_service.blog_service.entity.BlogEntity;
import com.blog_service.blog_service.entity.BlogSiteUserEntity;
import com.blog_service.blog_service.reposatory.BlogRepo;
import com.blog_service.blog_service.reposatory.BlogSiteUserRepo;

import jakarta.transaction.Transactional;

@Service
public class BlogSiteUserService {


	@Autowired
	private BlogRepo blogRepo;
	
	@Autowired
	private BlogSiteUserRepo reposatory;
	
	@Autowired
	private SiteUserFeingClient siteUserFeignClient;
	
	
	

	@Transactional
	public HashMap<String, Object> saveBlogSiteUser(TokenValidationRequest request) throws Exception {
		HashMap<String, Object> map = new HashMap<>();
		
		Integer blogId1 = Integer.valueOf(request.getBlogId());
		Integer siteUser = Integer.valueOf(request.getSiteUserId());

		Integer blogId = Optional.ofNullable(blogId1)

				.orElseThrow(() -> new BadRequestException("Blog Id is required"));

		Integer siteUserId = Optional.ofNullable(siteUser)

				.orElseThrow(() -> new BadRequestException("Site User Id is required"));

		BlogEntity blog = blogRepo.findById(blogId1).orElseThrow(() -> new BadRequestException("Blog does not exist"));
		Map<String, Object> siteUser1 = siteUserFeignClient.getUserById(siteUserId);

		if (siteUser1 == null || siteUser1.isEmpty()) {
		    throw new BadRequestException("Site user does not exist!!");
		}
		Optional<BlogSiteUserEntity> blogSiteUser = reposatory.findByBlogIdAndSiteUserId(blogId, siteUserId);
		if (blogSiteUser.isPresent()) {
			throw new RuntimeException("This blog is already saved!!");
		}

		BlogSiteUserEntity entity = new BlogSiteUserEntity();
		entity.setBlogId(blogId);
		entity.setSiteUserId(siteUserId);
		entity.setStatus((short) 1);
		entity.setModDate(new java.sql.Timestamp(System.currentTimeMillis()));
		entity.setRegDate(new java.sql.Timestamp(System.currentTimeMillis()));
		reposatory.save(entity);
		map.put("success", true);
		map.put("message", "Blog saved successfully");

		return map;
	}

	public List<BlogDTO> getBlogSiteUser(Integer blogSiteUserId) {
		List<BlogDTO> dto = reposatory.getBlogSiteUserById(blogSiteUserId);
		if (dto == null || dto.isEmpty()) {
			throw new RuntimeException("Blog site user detail not found!!");
		}
		return dto;
	}

	@Transactional
	public Map<String, Object> softDeleteBlogs(BlogSiteUserDto request) throws Exception {

		Map<String, Object> map = new HashMap<>();

		if (request.getSiteUserId() == null || request.getSiteUserId().trim().isEmpty()) {
			throw new BadRequestException("Site User Id is required");
		}

		Integer blogId = null;
		Integer siteUserId = Integer.valueOf(request.getSiteUserId());

		if (request.getBlogId() != null && !request.getBlogId().trim().isEmpty()) {
			blogId = Integer.valueOf(request.getBlogId());
		}

		long recordCount = reposatory.countActiveBlogs(siteUserId, blogId);

		if (recordCount == 0) {
			throw new BadRequestException("Blog site user does not exist");
		}

		int count = reposatory.softDeleteBlogs(siteUserId, blogId);

		map.put("success", true);

		if (blogId == null) {
			map.put("message", "All blogs removed successfully");
		} else {
			map.put("message", "Blog removed successfully");
		}

		map.put("deletedCount", count);

		return map;
	}
}
