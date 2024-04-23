package com.chenxi.finease.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.chenxi.finease.model.SavingsAccount;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long> {
    
    SavingsAccount findByAccountNumber(int accountNumber);

    @SuppressWarnings("null")
    @Query("SELECT ca FROM SavingsAccount ca WHERE ca.id <> 1")
    List<SavingsAccount> findAll();

    long count();
}