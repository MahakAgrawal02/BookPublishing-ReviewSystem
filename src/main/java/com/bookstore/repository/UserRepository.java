package com.bookstore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {

	Optional<UserEntity> findByUsername(String username);
	
	Boolean existsByUsername(String username);

	@Modifying
	@Query("delete from com.bookstore.entity.UserEntity u where username = :username")
	int deleteByUsername(String username);

}
