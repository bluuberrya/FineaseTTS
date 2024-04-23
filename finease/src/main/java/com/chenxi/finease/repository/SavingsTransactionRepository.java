package com.chenxi.finease.repository;

import com.chenxi.finease.model.SavingsTransaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface SavingsTransactionRepository extends JpaRepository<SavingsTransaction, Long> {

    @SuppressWarnings("null")
    List<SavingsTransaction> findAll();
    long count();
}
