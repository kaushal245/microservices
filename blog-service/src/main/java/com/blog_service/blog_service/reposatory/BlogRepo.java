package com.blog_service.blog_service.reposatory;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.blog_service.blog_service.dto.BlogCategoryDTO;
import com.blog_service.blog_service.dto.BlogDTO;
import com.blog_service.blog_service.entity.BlogEntity;


@Repository
public interface BlogRepo extends JpaRepository<BlogEntity, Integer> {

	@Query("SELECT new com.blog_service.blog_service.dto.BlogDTO(b.blogId, b.title, b.shortDesc, b.keywords, b.readTime, b.teamName, bc.blogCategoryName, b.image, b.description, "
			+ "b.status,  b.approval, b.userId, b.trendingFlag, b.editorFlag, b.counter, b.date, b.imgCaption  ) "
			+ "  FROM BlogEntity b join BlogCategoryEntity bc on bc.blogCategoryId = b.blogCategoryId "
			+ "  WHERE (:statusIndex = 0 OR b.status = :statusIndex) "
			+ "  AND b.status NOT IN (2, 3) AND b.publishFlagId IS NOT NULL AND b.publishFlagId IN (2)  "
			+ "  AND (:blogCategoryId IS NULL OR b.blogCategoryId = :blogCategoryId) "
			+ "  AND (:search IS NULL OR :search = '' OR LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%'))) "
			+ "  ORDER BY b.date DESC")
	Page<BlogDTO> getBlogDetails(Pageable pageable, @Param("statusIndex") int statusIndex,
			@Param("search") String search, @Param("blogCategoryId") Integer blogCategoryId);

	@Query("SELECT new com.blog_service.blog_service.dto.BlogDTO(  b.blogId, b.title, b.readTime, "
			+ " b.blogCategoryId, c.blogCategoryName, " + " b.trendingFlag, b.editorFlag, b.counter, b.date) "
			+ "FROM BlogEntity b " + "LEFT JOIN BlogCategoryEntity c ON c.blogCategoryId = b.blogCategoryId "
			+ " WHERE b.status NOT IN (2, 3) And b.publishFlagId = 2  ORDER BY b.date DESC")
	List<BlogDTO> getLatestBlogs(Pageable pageable);

	@Query("SELECT new com.blog_service.blog_service.dto.BlogDTO( "
			+ "b.blogId, b.title, b.shortDesc, b.keywords, b.readTime, b.teamName, "
			+ "bc.blogCategoryId, bc.blogCategoryName, b.description, b.status, b.trendingFlag, b.date) "
			+ "FROM BlogEntity b " + "JOIN BlogCategoryEntity bc ON bc.blogCategoryId = b.blogCategoryId "
			+ "WHERE b.status NOT IN (2, 3) And b.publishFlagId = 2  "
			+ "AND (:blogCategoryId IS NULL OR b.blogCategoryId = :blogCategoryId) "
			+ "  AND (:minReadTime IS NULL OR b.readTime >= :minReadTime) "
			+ "  AND (:maxReadTime IS NULL OR b.readTime <= :maxReadTime) "
			+ "  AND (:startDate IS NULL OR b.date >= :startDate)" + "AND (:endDate IS NULL OR b.date <= :endDate)"
			+ "  AND (:search IS NULL OR :search = '' OR "
			+ "      LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "      LOWER(b.shortDesc) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "      LOWER(b.keywords) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "      LOWER(b.description) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "      LOWER(bc.blogCategoryName) LIKE LOWER(CONCAT('%', :search, '%')) ) " + "ORDER BY b.date DESC")
	Page<BlogDTO> getBlogForSearch(Pageable pageable, @Param("search") String search,
			@Param("blogCategoryId") Integer blogCategoryId, @Param("minReadTime") Integer minReadTime,
			@Param("maxReadTime") Integer maxReadTime, @Param("startDate") Timestamp startDate,
			@Param("endDate") Timestamp endDate);

	@Query("SELECT new com.blog_service.blog_service.dto.BlogDTO( "
			+ "b.blogId, b.title, b.shortDesc, b.keywords, b.readTime, "
			+ "b.teamName, c.blogCategoryName, b.image, b.description, "
			+ "b.status, b.approval, b.userId, b.trendingFlag, "
			+ "b.editorFlag, b.counter, b.date, b.imgCaption, b.blogCategoryId ) " + "FROM BlogEntity b "
			+ "LEFT JOIN BlogCategoryEntity c ON c.blogCategoryId = b.blogCategoryId "
			+ "WHERE b.blogId = :blogId AND b.status =1")
	BlogDTO getBlogById(@Param("blogId") Integer blogId);

	@Modifying
	@jakarta.transaction.Transactional
	@Query("UPDATE BlogEntity b SET b.counter = COALESCE(b.counter, 0) + 1 WHERE b.blogId = :blogId")
	void incrementCounter(@Param("blogId") Integer blogId);

	@Query("SELECT new com.blog_service.blog_service.dto.BlogCategoryDTO(bc.blogCategoryId, bc.blogCategoryName) "
			+ "FROM BlogCategoryEntity bc WHERE bc.status = 1 order by bc.blogCategoryName")
	List<BlogCategoryDTO> findAllActiveBlogCategoryIdAndName();

	@Query(value = "select  i_blogid, d_date , s_title , s_shortdesc ,  s_image from t_blog where i_status = 1  and i_blogcategoryid = :blogCategoryId order by d_date desc ", nativeQuery = true)
	List<Map<String, Object>> getReadNextData(@Param("blogCategoryId") Integer blogCategoryId);

	@Query("SELECT b.metaTag FROM BlogEntity b WHERE b.blogId = :blogId")
	Optional<String> findMetaTagByBlogId(@Param("blogId") Integer blogId);

	@Query("SELECT b.title FROM BlogEntity b WHERE b.blogId = :blogId")
	Optional<String> findBlogId(@Param("blogId") Integer blogId);
}
