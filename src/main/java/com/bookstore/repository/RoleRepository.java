package com.bookstore.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    
    // Find a role entity by its name
    Optional<Role> findByName(String name);
}
