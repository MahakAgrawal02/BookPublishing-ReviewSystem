package com.bookstore.services;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.bookstore.entity.UserEntity;

import lombok.Data;

@Data
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    private String username;
    
    @JsonIgnore
    private String password;
    
    private Collection<? extends GrantedAuthority> authorities;
    
    // Constructor to initialize UserDetailsImpl with essential user info and authorities
    public UserDetailsImpl(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }
    
    // Static factory method to build UserDetailsImpl from UserEntity
    public static UserDetailsImpl build(UserEntity userEntity) {
        List<GrantedAuthority> authorities = userEntity.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        
        return new UserDetailsImpl(
                userEntity.getUserId(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                authorities);
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return authorities;
    }
    
    public Long getId() {
        return id;
    }

    // Account is never expired for this implementation
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    // Credentials are never expired for this implementation
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    // User is always enabled in this implementation
    @Override
    public boolean isEnabled() {
        return true;
    }

    // Account is never locked in this implementation
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
}
