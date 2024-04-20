package com.chenxi.finease.service.serviceImpl;

import com.chenxi.finease.model.User;
import com.chenxi.finease.repository.UserRepository;
import com.chenxi.finease.service.AccountService;
import com.chenxi.finease.service.UserService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    @Override
    public User createUser(User user) {
        User localUser = userRepository.findByUsername(user.getUsername());

        if (localUser != null) {
            
            logger.info("User with username {} already exist. ", user.getUsername());
            
        } else {
            user.setCurrentAccount(accountService.createCurrentAccount());
            user.setSavingsAccount(accountService.createSavingsAccount());
    
            localUser = userRepository.save(user);
        }
        return localUser;
    }
    
    public User updateUser(User updatedUser) {
        // Retrieve the user from the database
        User user = userRepository.findByUsername(updatedUser.getUsername());
        
        // Check if the user exists
        if (user != null) {
            // Update user information
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setEmail(updatedUser.getEmail());
            user.setPhoneNumber(updatedUser.getPhoneNumber());
            user.setPassword(updatedUser.getPassword());
    
            // Save the updated user to the database
            user = userRepository.save(user);
        } else {
            // Handle case where user is not found
            System.out.println("User not found!");
        }
    
        return user;
    }
    

    @Override
    public boolean validateUser(String username, String password) {

        User user = userRepository.findByUsername(username);
        
        // Check if the user exists and if the provided password matches
        if (user != null && user.getPassword().equals(password)) {
            return true; // User is valid
        }
        
        return false; // User is invalid
    }

    public User findByUsername(String username) {
    	
        return userRepository.findByUsername(username);
        
    }

    public User findByEmail(String email) {
    	
        return userRepository.findByEmail(email);
    }

    public boolean checkUserExists(String username, String email) {
    	
        return checkUsernameExists(username) || checkEmailExists(username);
        
    }

    public boolean checkUsernameExists(String username) {
    	
        return null != findByUsername(username);
        
    }

    public boolean checkEmailExists(String email) {
    	
        return null != findByEmail(email);
        

    }

    public void save(User user) {
    	
        userRepository.save(user);
        
    }

    public User saveUser(User user) {
    	
        return userRepository.save(user);
        
    }

    public List<User> findUserList() {
    	
        return userRepository.findAll();
        
    }

}



