package com.chenxi.finease.service.serviceImpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chenxi.finease.model.CurrentAccount;
import com.chenxi.finease.model.CurrentTransaction;
import com.chenxi.finease.model.SavingsAccount;
import com.chenxi.finease.model.SavingsTransaction;
import com.chenxi.finease.model.User;
import com.chenxi.finease.repository.CurrentAccountRepository;
import com.chenxi.finease.repository.SavingsAccountRepository;
import com.chenxi.finease.service.AccountService;
import com.chenxi.finease.service.TransactionService;
import com.itextpdf.text.DocumentException;

@Service
public class AccountServiceImpl implements AccountService{

    @Autowired
    private CurrentAccountRepository currentAccountRepository;

    @Autowired
    private SavingsAccountRepository savingsAccountRepository;

    @Autowired
    private TransactionService transactionService;

    public CurrentAccount createCurrentAccount() {
    	
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setAccountBalance(new BigDecimal(0.0));
        currentAccount.setAccountNumber(accountGen());

        currentAccountRepository.save(currentAccount);

        return currentAccountRepository.findByAccountNumber(currentAccount.getAccountNumber());
        
    }

    public SavingsAccount createSavingsAccount() {
    	
        SavingsAccount savingsAccount = new SavingsAccount();
        savingsAccount.setAccountBalance(new BigDecimal(0.0));
        savingsAccount.setAccountNumber(accountGen());

        savingsAccountRepository.save(savingsAccount);

        return savingsAccountRepository.findByAccountNumber(savingsAccount.getAccountNumber());
        
    }

    private int accountGen() {

        return ThreadLocalRandom.current().nextInt(2323, 232321474);
    }

    public void deposit(String accountType, double amount, User user) throws FileNotFoundException, DocumentException, IOException {
        if (accountType.equalsIgnoreCase("Current")) {
            CurrentAccount currentAccount = user.getCurrentAccount();
            currentAccount.setAccountBalance(currentAccount.getAccountBalance().add(new BigDecimal(amount)));
            currentAccountRepository.save(currentAccount);
    
            Date date = new Date();
    
            CurrentTransaction currentTransaction = new CurrentTransaction(date, user.getUsername() + " deposit to Current Account", "Account", "Finished", amount, currentAccount.getAccountBalance(), currentAccount);
            transactionService.saveCurrentDepositTransaction(currentTransaction);

            transactionService.generateCurrentReceipt(currentTransaction);
    
        } else if (accountType.equalsIgnoreCase("Savings")) {
            SavingsAccount savingsAccount = user.getSavingsAccount();
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
            savingsAccountRepository.save(savingsAccount);
    
            Date date = new Date();
            SavingsTransaction savingsTransaction = new SavingsTransaction(date, user.getUsername() + " deposit to Savings Account", "Account", "Finished", amount, savingsAccount.getAccountBalance(), savingsAccount);
            transactionService.saveSavingsDepositTransaction(savingsTransaction);

            transactionService.generateSavingsReceipt(savingsTransaction);
        }
        
    }
    
    public void withdraw(String accountType, double amount, User user) throws FileNotFoundException, DocumentException, IOException {
        if (accountType.equalsIgnoreCase("Current")) {
            CurrentAccount currentAccount = user.getCurrentAccount();
            
            currentAccount.setAccountBalance(currentAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            currentAccountRepository.save(currentAccount);
    
            Date date = new Date();

            CurrentTransaction currentTransaction = new CurrentTransaction(date, user.getUsername() + " withdraw from Current Account", "Account", "Finished", amount, currentAccount.getAccountBalance(), currentAccount);
            transactionService.saveCurrentWithdrawTransaction(currentTransaction);

            transactionService.generateCurrentReceipt(currentTransaction);
            
        } else if (accountType.equalsIgnoreCase("Savings")) {
            SavingsAccount savingsAccount = user.getSavingsAccount();
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            savingsAccountRepository.save(savingsAccount);
    
            Date date = new Date();
            SavingsTransaction savingsTransaction = new SavingsTransaction(date, user.getUsername() + " withdraw from savings Account", "Account", "Finished", amount, savingsAccount.getAccountBalance(), savingsAccount);
            transactionService.saveSavingsWithdrawTransaction(savingsTransaction);

            transactionService.generateSavingsReceipt(savingsTransaction);
        }
    }

    public List<SavingsAccount> findAllSavingsAccountList() {
    	
        return savingsAccountRepository.findAll();
        
    }

    public List<CurrentAccount> findAllCurrentAccountList() {
    	
        return currentAccountRepository.findAll();
        
    }

    @Override
    public int getTotalNumberOfSavingsAccounts() {
        return (int) savingsAccountRepository.count();
    }

    @Override
    public int getTotalNumberOfCurrentAccounts() {
        return (int) currentAccountRepository.count();
    }

    public boolean isBalanceSufficient(String accountType, double amount, User user) {
        if (accountType.equalsIgnoreCase("Current")) {
            CurrentAccount currentAccount = user.getCurrentAccount();
            BigDecimal currentBalance = currentAccount.getAccountBalance();
            return currentBalance.compareTo(BigDecimal.valueOf(amount)) >= 0;
        } else if (accountType.equalsIgnoreCase("Savings")) {
            SavingsAccount savingsAccount = user.getSavingsAccount();
            BigDecimal savingsBalance = savingsAccount.getAccountBalance();
            return savingsBalance.compareTo(BigDecimal.valueOf(amount)) >= 0;
        }
        return false;
    }    

}
