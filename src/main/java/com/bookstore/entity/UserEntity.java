package com.bookstore.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Represents a user entity with common user fields and roles.
 * Uses single table inheritance strategy to allow subclasses like Author.
 */
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class UserEntity {

    /**
     * Primary key for UserEntity.
     * Auto-generated value.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    /**
     * Username of the user.
     * Unique constraint ensures no duplicates.
     */
    @Column(unique = true)
    private String username;

    /**
     * Password for user authentication.
     */
    @Column
    private String password;

    /**
     * Email of the user.
     */
    @Column
    private String email;

    /**
     * Roles assigned to the user.
     * Many-to-many relationship with Role entity.
     * Uses a join table named 'user_roles' with user_id and role_id columns.
     * Cascade persist and merge operations to propagate changes.
     */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();

    /**
     * Convenience constructor to create a user with username and password.
     * 
     * @param username the username
     * @param password the password
     */
    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
