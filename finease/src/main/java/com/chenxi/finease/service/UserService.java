package com.chenxi.finease.service;

import java.util.List;

import com.chenxi.finease.model.User;

public interface UserService {
    
    User findByUserId(Long id);

    User findByUsername(String username);

    User findByEmail(String email);

    boolean validateUser(String username, String password);

    boolean checkUserExists(String username, String email);

    boolean checkUsernameExists(String username);

    boolean checkEmailExists(String email);

    void save(User user);

    User createUser(User user);

    User updateUser(User updatedUser);

    User saveUser(User user);

    List<User> findUserList();

    List<User> getAllUsersExceptCurrentUser(String currentUser);

}
