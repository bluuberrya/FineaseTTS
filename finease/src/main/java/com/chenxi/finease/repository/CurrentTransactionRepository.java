package com.chenxi.finease.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chenxi.finease.model.CurrentTransaction;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface CurrentTransactionRepository extends JpaRepository<CurrentTransaction, Long> {
    
    @SuppressWarnings("null")
    List<CurrentTransaction> findAll();
    
}