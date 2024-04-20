package com.chenxi.finease.service.serviceImpl;

import java.math.BigDecimal;
<<<<<<< HEAD
=======
import java.security.Principal;
>>>>>>> FineaseTTS/main
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
import org.springframework.stereotype.Service;

=======
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.chenxi.finease.dao.CurrentAccountDao;
import com.chenxi.finease.dao.SavingsAccountDao;
>>>>>>> FineaseTTS/main
import com.chenxi.finease.model.CurrentAccount;
import com.chenxi.finease.model.CurrentTransaction;
import com.chenxi.finease.model.SavingsAccount;
import com.chenxi.finease.model.SavingsTransaction;
import com.chenxi.finease.model.User;
<<<<<<< HEAD
import com.chenxi.finease.repository.CurrentAccountRepository;
import com.chenxi.finease.repository.SavingsAccountRepository;
import com.chenxi.finease.service.AccountService;
import com.chenxi.finease.service.TransactionService;

@Service
public class AccountServiceImpl implements AccountService{

    @Autowired
    private CurrentAccountRepository currentAccountRepository;

    @Autowired
    private SavingsAccountRepository savingsAccountRepository;
=======
import com.chenxi.finease.service.AccountService;
import com.chenxi.finease.service.TransactionService;
import com.chenxi.finease.service.UserService;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private CurrentAccountDao currentAccountDao;

    @Autowired
    private SavingsAccountDao savingsAccountDao;

    @Autowired
    @Lazy
    private UserService userService;
>>>>>>> FineaseTTS/main

    @Autowired
    private TransactionService transactionService;

<<<<<<< HEAD

=======
>>>>>>> FineaseTTS/main
    public CurrentAccount createCurrentAccount() {
    	
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setAccountBalance(new BigDecimal(0.0));
        currentAccount.setAccountNumber(accountGen());

<<<<<<< HEAD
        currentAccountRepository.save(currentAccount);

        return currentAccountRepository.findByAccountNumber(currentAccount.getAccountNumber());
=======
        currentAccountDao.save(currentAccount);

        return currentAccountDao.findByAccountNumber(currentAccount.getAccountNumber());
>>>>>>> FineaseTTS/main
        
    }

    public SavingsAccount createSavingsAccount() {
    	
        SavingsAccount savingsAccount = new SavingsAccount();
        savingsAccount.setAccountBalance(new BigDecimal(0.0));
        savingsAccount.setAccountNumber(accountGen());

<<<<<<< HEAD
        savingsAccountRepository.save(savingsAccount);

        return savingsAccountRepository.findByAccountNumber(savingsAccount.getAccountNumber());
        
    }

    private int accountGen() {

        return ThreadLocalRandom.current().nextInt(2323, 232321474);
    }

    public void deposit(String accountType, double amount, User user) {
        if (accountType.equalsIgnoreCase("Current")) {
            CurrentAccount currentAccount = user.getCurrentAccount();
            currentAccount.setAccountBalance(currentAccount.getAccountBalance().add(new BigDecimal(amount)));
            currentAccountRepository.save(currentAccount);
    
            Date date = new Date();
    
            CurrentTransaction currentTransaction = new CurrentTransaction(date, "Deposit to Current Account", "Account", "Finished", amount, currentAccount.getAccountBalance(), currentAccount);
            transactionService.saveCurrentDepositTransaction(currentTransaction);
    
        } else if (accountType.equalsIgnoreCase("Savings")) {
            SavingsAccount savingsAccount = user.getSavingsAccount();
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
            savingsAccountRepository.save(savingsAccount);
    
            Date date = new Date();
            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Deposit to savings Account", "Account", "Finished", amount, savingsAccount.getAccountBalance(), savingsAccount);
            transactionService.saveSavingsDepositTransaction(savingsTransaction);
        }
    }
    
    public void withdraw(String accountType, double amount, User user) {
        if (accountType.equalsIgnoreCase("Current")) {
            CurrentAccount currentAccount = user.getCurrentAccount();
            currentAccount.setAccountBalance(currentAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            currentAccountRepository.save(currentAccount);
    
            Date date = new Date();
    
=======
        savingsAccountDao.save(savingsAccount);

        return savingsAccountDao.findByAccountNumber(savingsAccount.getAccountNumber());
        
    }

    public void deposit(String accountType, double amount, Principal principal) {
    	
        User user = userService.findByUsername(principal.getName());

        if (accountType.equalsIgnoreCase("Current")) {
            CurrentAccount currentAccount = user.getCurrentAccount();
            currentAccount.setAccountBalance(currentAccount.getAccountBalance().add(new BigDecimal(amount)));
            currentAccountDao.save(currentAccount);

            Date date = new Date();

            CurrentTransaction currentTransaction = new CurrentTransaction(date, "Deposit to Current Account", "Account", "Finished", amount, currentAccount.getAccountBalance(), currentAccount);
            transactionService.saveCurrentDepositTransaction(currentTransaction);

        } else if (accountType.equalsIgnoreCase("Savings")) {
        	
            SavingsAccount savingsAccount = user.getSavingsAccount();
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
            savingsAccountDao.save(savingsAccount);

            Date date = new Date();
            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Deposit to savings Account", "Account", "Finished", amount, savingsAccount.getAccountBalance(), savingsAccount);
            transactionService.saveSavingsDepositTransaction(savingsTransaction);
            
        }
        
    }

    public void withdraw(String accountType, double amount, Principal principal) {
    	
        User user = userService.findByUsername(principal.getName());

        if (accountType.equalsIgnoreCase("Current")) {
            CurrentAccount currentAccount = user.getCurrentAccount();
            currentAccount.setAccountBalance(currentAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            currentAccountDao.save(currentAccount);

            Date date = new Date();

>>>>>>> FineaseTTS/main
            CurrentTransaction currentTransaction = new CurrentTransaction(date, "Withdraw from Current Account", "Account", "Finished", amount, currentAccount.getAccountBalance(), currentAccount);
            transactionService.saveCurrentWithdrawTransaction(currentTransaction);
            
        } else if (accountType.equalsIgnoreCase("Savings")) {
            SavingsAccount savingsAccount = user.getSavingsAccount();
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
<<<<<<< HEAD
            savingsAccountRepository.save(savingsAccount);
    
            Date date = new Date();
            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Withdraw from savings Account", "Account", "Finished", amount, savingsAccount.getAccountBalance(), savingsAccount);
            transactionService.saveSavingsWithdrawTransaction(savingsTransaction);
        }
    }    
    
}
=======
            savingsAccountDao.save(savingsAccount);

            Date date = new Date();
            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Withdraw from savings Account", "Account", "Finished", amount, savingsAccount.getAccountBalance(), savingsAccount);
            transactionService.saveSavingsWithdrawTransaction(savingsTransaction);
            
        }
        
    }

    private int accountGen() {
    	
        return ThreadLocalRandom.current().nextInt(2323, 232321474);
        
    }

}
>>>>>>> FineaseTTS/main
