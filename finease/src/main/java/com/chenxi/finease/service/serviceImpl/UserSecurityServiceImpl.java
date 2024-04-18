package com.chenxi.finease.service.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.chenxi.finease.dao.UserDao;
import com.chenxi.finease.model.User;

@Service
public class UserSecurityServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserSecurityServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	
        User user = userDao.findByUsername(username);
        
        if (user == null) {
        	
            logger.warn("Username {} not found", username);
            throw new UsernameNotFoundException("Username " + username + " not found");
            
        }
        
        return user;
        
    }
    
}
