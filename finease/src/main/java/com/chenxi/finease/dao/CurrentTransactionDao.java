package com.chenxi.finease.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.chenxi.finease.model.CurrentTransaction;

public interface CurrentTransactionDao extends CrudRepository<CurrentTransaction, Long> {
    @SuppressWarnings("null")
    List<CurrentTransaction> findAll();
    
}