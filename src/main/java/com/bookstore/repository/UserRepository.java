package com.bookstore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {

    // Retrieve a user by their unique username
    Optional<UserEntity> findByUsername(String username);
    
    // Check if a user exists by their username
    Boolean existsByUsername(String username);

    // Delete a user entity based on username
    @Modifying
    @Query("delete from com.bookstore.entity.UserEntity u where username = :username")
    int deleteByUsername(String username);
}
