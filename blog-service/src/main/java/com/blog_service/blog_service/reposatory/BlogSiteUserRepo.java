package com.blog_service.blog_service.reposatory;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.blog_service.blog_service.dto.BlogDTO;
import com.blog_service.blog_service.entity.BlogSiteUserEntity;

import jakarta.transaction.Transactional;


@Repository
public interface BlogSiteUserRepo extends JpaRepository<BlogSiteUserEntity, Integer> {
	
	@Query("SELECT b FROM BlogSiteUserEntity b " +
		       "WHERE b.siteUserId = :siteUserId " +
		       "AND b.blogId = :blogId " +
		       "AND b.status = 1" +
		       "ORDER BY b.status "
			)
	Optional<BlogSiteUserEntity> findByBlogIdAndSiteUserId(
		        @Param("blogId") Integer blogId,
		        @Param("siteUserId") Integer siteUserId);
	
	@Query("SELECT new com.blog_service.blog_service.dto.BlogDTO( " + "bsu.blogSiteUserId, " + "blog.blogId, "
			+ "bsu.siteUserId, " + "blog.title, " + "blog.shortDesc, " + "blog.keywords, " + "blog.readTime, "
			+ "blog.teamName, " + "blog.blogCategoryId, " + "bc.blogCategoryName, " + "blog.image, "
			+ "blog.description, " + "blog.approval, " + "blog.trendingFlag, " + "blog.editorFlag, " 
			+ "blog.counter, "
			+ "blog.date, " + "blog.imgCaption,blog.applicablefor,bsu.status,blog.status,blog.publishFlagId ) " + "FROM BlogSiteUserEntity bsu "
			+ "LEFT JOIN BlogEntity blog ON blog.blogId = bsu.blogId "
			+ "LEFT JOIN BlogCategoryEntity bc ON bc.blogCategoryId = blog.blogCategoryId "
			+ "WHERE bsu.siteUserId = :blogSiteUserId AND blog.applicablefor IN  (2,3) AND blog.publishFlagId = 2 AND blog.status =1 AND bsu.status =1 ")
	List<BlogDTO> getBlogSiteUserById(@Param("blogSiteUserId") Integer blogSiteUserId);
	
	
	@Query("SELECT new com.blog_service.blog_service.dto.BlogDTO( " + "bsu.blogSiteUserId, " + "blog.blogId, "
			+ "bsu.siteUserId, " + "blog.title, " + "blog.shortDesc, " + "blog.keywords, " + "blog.readTime, "
			+ "blog.teamName, " + "blog.blogCategoryId, " + "bc.blogCategoryName, " + "blog.image, "
			+ "blog.description, " + "blog.approval, " + "blog.trendingFlag, " + "blog.editorFlag, " 
			+ "blog.counter, "
			+ "blog.date, " + "blog.imgCaption,blog.applicablefor,bsu.status,blog.status,blog.publishFlagId ) " + "FROM BlogSiteUserEntity bsu "
			+ "LEFT JOIN BlogEntity blog ON blog.blogId = bsu.blogId "
			+ "LEFT JOIN BlogCategoryEntity bc ON bc.blogCategoryId = blog.blogCategoryId "
			+ "WHERE bsu.siteUserId = :blogSiteUserId AND blog.applicablefor IN  (2,3) AND blog.publishFlagId = 2 AND blog.status =1 AND bsu.status =1 ")
	List<BlogDTO> getBlogSiteUserById1(@Param("blogSiteUserId") Integer blogSiteUserId);
	
	
	@Modifying
	@Transactional
	@Query("UPDATE BlogSiteUserEntity b " +
	       "SET b.status = 2, b.modDate = CURRENT_TIMESTAMP " +
	       "WHERE b.siteUserId = :siteUserId " +
	       "AND (:blogId IS NULL OR b.blogId = :blogId)")
	int softDeleteBlogs(@Param("siteUserId") Integer siteUserId,
	                    @Param("blogId") Integer blogId);
	
	@Query("SELECT COUNT(b) FROM BlogSiteUserEntity b " +
		       "WHERE b.siteUserId = :siteUserId " +
		       "AND (:blogId IS NULL OR b.blogId = :blogId) " +
		       "AND b.status = 1")
		long countActiveBlogs(@Param("siteUserId") Integer siteUserId,
		                      @Param("blogId") Integer blogId);
}