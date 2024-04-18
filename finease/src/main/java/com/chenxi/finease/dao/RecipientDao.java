package com.chenxi.finease.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.chenxi.finease.model.Recipient;

public interface RecipientDao extends CrudRepository<Recipient, Long> {
<<<<<<< HEAD
    @SuppressWarnings("null")
    List<Recipient> findAll();

    Recipient findByName(String recipientName);

=======

    List<Recipient> findAll();

    Recipient findByName(String recipientName);

>>>>>>> parent of 06fb326 (create fineasedb on mySQL. issue with dao files)
    void deleteByName(String recipientName);
}