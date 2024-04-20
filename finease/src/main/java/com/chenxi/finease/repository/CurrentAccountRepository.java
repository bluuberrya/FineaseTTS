package com.chenxi.finease.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chenxi.finease.model.CurrentAccount;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface CurrentAccountRepository extends JpaRepository<CurrentAccount, Long> {
    
    CurrentAccount findByAccountNumber(int accountNumber);

}