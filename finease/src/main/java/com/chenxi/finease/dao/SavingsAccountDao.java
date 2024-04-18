package com.chenxi.finease.dao;

import org.springframework.data.repository.CrudRepository;

import com.chenxi.finease.model.SavingsAccount;

public interface SavingsAccountDao extends CrudRepository<SavingsAccount, Long> {

    SavingsAccount findByAccountNumber(int accountNumber);
    
}
