package com.chenxi.finease.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chenxi.finease.model.Recipient;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface RecipientRepository extends JpaRepository <Recipient, Long>{

    @SuppressWarnings("null")
    List<Recipient> findAll();

    Recipient findByName(String recipientName);

    void deleteByName(String recipientName);

}
