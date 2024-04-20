package com.chenxi.finease.service.serviceImpl;

<<<<<<< HEAD
import com.chenxi.finease.model.User;
import com.chenxi.finease.repository.UserRepository;
import com.chenxi.finease.service.AccountService;
import com.chenxi.finease.service.UserService;

import java.util.List;
=======
import java.util.List;
import java.util.Set;
>>>>>>> FineaseTTS/main

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
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
=======
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chenxi.finease.dao.RoleDao;
import com.chenxi.finease.dao.UserDao;
import com.chenxi.finease.model.User;
import com.chenxi.finease.security.UserRole;
import com.chenxi.finease.service.AccountService;
import com.chenxi.finease.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    private AccountService accountService;

    public void save(User user) {
    	
        userDao.save(user);
        
>>>>>>> FineaseTTS/main
    }

    public User findByUsername(String username) {
    	
<<<<<<< HEAD
        return userRepository.findByUsername(username);
=======
        return userDao.findByUsername(username);
>>>>>>> FineaseTTS/main
        
    }

    public User findByEmail(String email) {
    	
<<<<<<< HEAD
        return userRepository.findByEmail(email);
=======
        return userDao.findByEmail(email);
    }


    public User createUser(User user, Set<UserRole> userRoles) {
    	
        User localUser = userDao.findByUsername(user.getUsername());

        if (localUser != null) {
        	
            logger.info("User with username {} already exist. Nothing will be done. ", user.getUsername());
            
        } else {
        	
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);

            for (UserRole ur : userRoles) {
            	
                roleDao.save(ur.getRole());
                
            }

            user.getUserRoles().addAll(userRoles);

            user.setCurrentAccount(accountService.createCurrentAccount());
            user.setSavingsAccount(accountService.createSavingsAccount());

            localUser = userDao.save(user);
        }

        return localUser;
        
>>>>>>> FineaseTTS/main
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

<<<<<<< HEAD
    public void save(User user) {
    	
        userRepository.save(user);
        
    }

    public User saveUser(User user) {
    	
        return userRepository.save(user);
=======
    public User saveUser(User user) {
    	
        return userDao.save(user);
>>>>>>> FineaseTTS/main
        
    }

    public List<User> findUserList() {
    	
<<<<<<< HEAD
        return userRepository.findAll();
        
    }

}



=======
        return userDao.findAll();
        
    }

    public void enableUser(String username) {
    	
        User user = findByUsername(username);
        user.setEnabled(true);
        userDao.save(user);
        
    }

    public void disableUser(String username) {
    	
        User user = findByUsername(username);
        user.setEnabled(false);
        logger.info("user status : {}", user.isEnabled());
        userDao.save(user);
        logger.info(username, "{}, is disabled.");
        
    }
    
}
>>>>>>> FineaseTTS/main
