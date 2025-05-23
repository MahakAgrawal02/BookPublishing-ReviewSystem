package com.bookstore.services;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bookstore.entity.UserEntity;
import com.bookstore.entity.Role;
import com.bookstore.payload.request.UserRequest;
import com.bookstore.repository.RoleRepository;
import com.bookstore.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepo;
    
    @Autowired
    private PasswordEncoder pwdEncoder;
    
    /**
     * Saves a new user with encoded password and assigned role.
     * If a user with the same username exists, returns false.
     * 
     * @param userInfo DTO containing username, password, and roleName
     * @return true if user saved successfully; false if username already exists
     */
    public boolean saveUser(UserRequest userInfo) {
        UserEntity userEntity = new UserEntity();

        userEntity.setUsername(userInfo.getUsername());
        userEntity.setPassword(pwdEncoder.encode(userInfo.getPassword()));
        
        String roleName = userInfo.getRoleName();
        // Retrieve role by name or create a new Role entity if not found
        Role roles = roleRepo.findByName(roleName)
                .orElse(new Role(roleName));
        
        userEntity.setRoles(Collections.singletonList(roles));

        try {
            userRepository.save(userEntity);
        } catch (DataIntegrityViolationException ex) {
            // Likely caused by duplicate username (unique constraint)
            return false;
        }
        
        return true;
    }
    
    /**
     * Loads user details by username for Spring Security authentication.
     * Throws UsernameNotFoundException if user does not exist.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " is not found."));
        
        return UserDetailsImpl.build(userEntity);
    }
    
    /**
     * Deletes user by username.
     * Returns the number of rows deleted (0 or 1).
     */
    @Transactional
    public int deleteUser(String username) {
        return userRepository.deleteByUsername(username);
    }
    
}
