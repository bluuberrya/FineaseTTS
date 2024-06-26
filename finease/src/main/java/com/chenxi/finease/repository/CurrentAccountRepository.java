package com.chenxi.finease.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.chenxi.finease.model.CurrentAccount;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface CurrentAccountRepository extends JpaRepository<CurrentAccount, Long> {
    
    CurrentAccount findByAccountNumber(int accountNumber);

    @SuppressWarnings("null")
    @Query("SELECT ca FROM CurrentAccount ca WHERE ca.id <> 1")
    List<CurrentAccount> findAll();

    long count();
}