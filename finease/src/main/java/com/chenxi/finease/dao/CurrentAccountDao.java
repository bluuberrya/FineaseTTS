package com.chenxi.finease.dao;

import org.springframework.data.repository.CrudRepository;

import com.chenxi.finease.model.CurrentAccount;

public interface CurrentAccountDao extends CrudRepository<CurrentAccount, Long> {

	CurrentAccount findByAccountNumber(int accountNumber);
	
}