package com.chenxi.finease.service.serviceImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chenxi.finease.service.AdminService;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {
	
	public boolean deactivateAccount(String username) {
		
		boolean status = false;
		
		//List<User> enabledUsers = userService.findUserList();
		
		//enabledUsers.get(username);
		
		return status;
	}

}
