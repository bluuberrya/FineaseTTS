package com.chenxi.finease.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chenxi.finease.model.SavingsAccount;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long> {
    
    SavingsAccount findByAccountNumber(int accountNumber);

}