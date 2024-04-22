package com.chenxi.finease.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.chenxi.finease.model.CurrentAccount;
import com.chenxi.finease.model.SavingsAccount;
import com.chenxi.finease.model.User;
import com.itextpdf.text.DocumentException;

public interface AccountService {

    CurrentAccount createCurrentAccount();

    SavingsAccount createSavingsAccount();
    
    void deposit(String accountType, double amount, User user) throws FileNotFoundException, DocumentException, IOException;
    
    void withdraw(String accountType, double amount, User user) throws FileNotFoundException, DocumentException, IOException;

    List<SavingsAccount> findAllSavingsAccountList();

    List<CurrentAccount> findAllCurrentAccountList();

}