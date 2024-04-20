package com.chenxi.finease.service;

import java.util.List;
<<<<<<< HEAD

import com.chenxi.finease.model.User;

public interface UserService {
    
=======
import java.util.Set;

import org.springframework.stereotype.Service;

import com.chenxi.finease.model.User;
import com.chenxi.finease.security.UserRole;

@Service("userDetailsService")
public interface UserService {

>>>>>>> FineaseTTS/main
    User findByUsername(String username);

    User findByEmail(String email);

<<<<<<< HEAD
    boolean validateUser(String username, String password);

=======
>>>>>>> FineaseTTS/main
    boolean checkUserExists(String username, String email);

    boolean checkUsernameExists(String username);

    boolean checkEmailExists(String email);

    void save(User user);

<<<<<<< HEAD
    User createUser(User user);

    User updateUser(User updatedUser);
=======
    User createUser(User user, Set<UserRole> userRoles);
>>>>>>> FineaseTTS/main

    User saveUser(User user);

    List<User> findUserList();

<<<<<<< HEAD

}



=======
    void enableUser(String username);

    void disableUser(String username);

}
>>>>>>> FineaseTTS/main
