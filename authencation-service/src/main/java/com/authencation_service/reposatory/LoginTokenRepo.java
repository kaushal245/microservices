package com.authencation_service.reposatory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.authencation_service.entity.LoginTokenEntity;


@Repository
public interface LoginTokenRepo extends JpaRepository<LoginTokenEntity, Integer> {
	@Query("SELECT m FROM LoginTokenEntity m WHERE m.identifier = :identifier AND m.status = 1 ORDER BY m.status DESC")
	List<LoginTokenEntity> findByIdentifier(@Param("identifier") String identifier);

	@Query("SELECT m FROM LoginTokenEntity m " + "WHERE m.identifier = :identifier " + "AND m.token = :token "
			+ "AND m.status = 1")
	Optional<LoginTokenEntity> findByIdentifierAndToken(@Param("identifier") String identifier,
			@Param("token") String token);

	@Modifying
	@Transactional
	@Query("UPDATE LoginTokenEntity l " + "SET l.status = 2 " + "WHERE l.identifier = :identifier "
			+ "AND l.token = :token")
	int updateTokenStatus(@Param("identifier") String identifier, @Param("token") String token);
}
