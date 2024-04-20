package com.chenxi.finease.service;

import com.chenxi.finease.model.CurrentAccount;
import com.chenxi.finease.model.SavingsAccount;
import com.chenxi.finease.model.User;

public interface AccountService {

    CurrentAccount createCurrentAccount();

    SavingsAccount createSavingsAccount();
    
    void deposit(String accountType, double amount, User user);
    
    void withdraw(String accountType, double amount, User user);

}