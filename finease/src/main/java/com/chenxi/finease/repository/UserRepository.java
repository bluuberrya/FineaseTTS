// UserRepository.java
package com.chenxi.finease.repository;

import com.chenxi.finease.model.User;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserId(Long id);
    User findByUsername(String username);
    User findByEmail(String email);
    
    @SuppressWarnings("null")
    List<User> findAll();

    @Query("SELECT u FROM User u WHERE u.username <> :currentUsername AND u.username <> 'admin'")
    List<User> findAllExceptCurrentUser(@Param("currentUsername") String currentUser);

}