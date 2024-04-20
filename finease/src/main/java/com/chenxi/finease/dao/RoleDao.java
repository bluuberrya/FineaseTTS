package com.chenxi.finease.dao;

import org.springframework.data.repository.CrudRepository;

import com.chenxi.finease.security.Role;

public interface RoleDao extends CrudRepository<Role, Integer> {

    Role findByName(String name);
    
}