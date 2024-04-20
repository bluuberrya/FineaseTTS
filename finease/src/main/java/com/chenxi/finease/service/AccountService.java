package com.chenxi.finease.service;

<<<<<<< HEAD
import com.chenxi.finease.model.CurrentAccount;
import com.chenxi.finease.model.SavingsAccount;
import com.chenxi.finease.model.User;
=======
import java.security.Principal;

import com.chenxi.finease.model.CurrentAccount;
import com.chenxi.finease.model.SavingsAccount;
>>>>>>> FineaseTTS/main

public interface AccountService {

    CurrentAccount createCurrentAccount();

    SavingsAccount createSavingsAccount();
<<<<<<< HEAD
    
    void deposit(String accountType, double amount, User user);
    
    void withdraw(String accountType, double amount, User user);

}
=======

    void deposit(String accountType, double amount, Principal principal);

    void withdraw(String accountType, double amount, Principal principal);

}
>>>>>>> FineaseTTS/main
