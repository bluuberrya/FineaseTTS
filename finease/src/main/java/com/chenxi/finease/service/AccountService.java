package com.chenxi.finease.service;

import java.security.Principal;

import com.chenxi.finease.model.CurrentAccount;
import com.chenxi.finease.model.SavingsAccount;

public interface AccountService {

    CurrentAccount createCurrentAccount();

    SavingsAccount createSavingsAccount();

    void deposit(String accountType, double amount, Principal principal);

    void withdraw(String accountType, double amount, Principal principal);

}
