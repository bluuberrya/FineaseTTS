// UserRepository.java
package com.chenxi.finease.repository;

import com.chenxi.finease.model.User;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    User findByEmail(String email);
    
    @SuppressWarnings("null")
    List<User> findAll();
}