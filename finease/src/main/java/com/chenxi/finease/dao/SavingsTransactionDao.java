package com.chenxi.finease.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.chenxi.finease.model.SavingsTransaction;

public interface SavingsTransactionDao extends CrudRepository<SavingsTransaction, Long> {
    @SuppressWarnings("null")
    List<SavingsTransaction> findAll();
    
}