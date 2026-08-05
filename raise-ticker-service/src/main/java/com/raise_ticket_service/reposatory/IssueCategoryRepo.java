package com.raise_ticket_service.reposatory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.raise_ticket_service.entities.IssueCategoryEntity;

@Repository
public interface IssueCategoryRepo extends JpaRepository<IssueCategoryEntity, Integer> {

	  @Query(value = "SELECT * FROM t_issuecategory", nativeQuery = true)
	  List<IssueCategoryEntity> issueCategoryList();
		
	  List<IssueCategoryEntity> findByNameContainingIgnoreCase(String name);
}
