package com.chenxi.finease.service.serviceImpl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chenxi.finease.model.CurrentAccount;
import com.chenxi.finease.model.CurrentTransaction;
import com.chenxi.finease.model.Recipient;
import com.chenxi.finease.model.SavingsAccount;
import com.chenxi.finease.model.SavingsTransaction;
import com.chenxi.finease.model.User;
import com.chenxi.finease.repository.CurrentAccountRepository;
import com.chenxi.finease.repository.CurrentTransactionRepository;
import com.chenxi.finease.repository.RecipientRepository;
import com.chenxi.finease.repository.SavingsAccountRepository;
import com.chenxi.finease.repository.SavingsTransactionRepository;
import com.chenxi.finease.service.TransactionService;
import com.chenxi.finease.service.UserService;

@Service
public class TransactionServiceImpl implements TransactionService {
    
    @Autowired
    private UserService userService;

    @Autowired
    private CurrentTransactionRepository currentTransactionRepository;

    @Autowired
    private SavingsTransactionRepository savingsTransactionRepository;

    @Autowired
    private CurrentAccountRepository currentAccountRepository;

    @Autowired
    private SavingsAccountRepository savingsAccountRepository;

    @Autowired
    private RecipientRepository recipientRepository;


    public List<CurrentTransaction> findCurrentTransactionList(String username) {
    	
        User user = userService.findByUsername(username);
        List<CurrentTransaction> currentTransactionList = user.getCurrentAccount().getCurrentTransactionList();

        return currentTransactionList;
        
    }

    public List<SavingsTransaction> findSavingsTransactionList(String username) {
    	
        User user = userService.findByUsername(username);
        List<SavingsTransaction> savingsTransactionList = user.getSavingsAccount().getSavingsTransactionList();

        return savingsTransactionList;
    }

    public void saveCurrentDepositTransaction(CurrentTransaction currentTransaction) {
    	
        currentTransactionRepository.save(currentTransaction);
        
    }

    public void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction) {
    	
        savingsTransactionRepository.save(savingsTransaction);
        
    }

    public void saveCurrentWithdrawTransaction(CurrentTransaction currentTransaction) {
    	
        currentTransactionRepository.save(currentTransaction);
        
    }

    public void saveSavingsWithdrawTransaction(SavingsTransaction savingsTransaction) {
    	
        savingsTransactionRepository.save(savingsTransaction);
        
    }

    public void betweenAccountsTransfer(String transferFrom, String transferTo, String amount, CurrentAccount currentAccount, SavingsAccount savingsAccount) throws Exception {
    	
        if (transferFrom.equalsIgnoreCase("Current") && transferTo.equalsIgnoreCase("Savings")) {
            currentAccount.setAccountBalance(currentAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
            currentAccountRepository.save(currentAccount);
            savingsAccountRepository.save(savingsAccount);

            Date date = new Date();

            CurrentTransaction currentTransaction = new CurrentTransaction(date, "Between account transfer from " + transferFrom + " to " + transferTo, "Account", "Finished", Double.parseDouble(amount), currentAccount.getAccountBalance(), currentAccount);
            currentTransactionRepository.save(currentTransaction);
            
        } else if (transferFrom.equalsIgnoreCase("Savings") && transferTo.equalsIgnoreCase("Current")) {
        	
            currentAccount.setAccountBalance(currentAccount.getAccountBalance().add(new BigDecimal(amount)));
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            currentAccountRepository.save(currentAccount);
            savingsAccountRepository.save(savingsAccount);

            Date date = new Date();

            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Between account transfer from " + transferFrom + " to " + transferTo, "Transfer", "Finished", Double.parseDouble(amount), savingsAccount.getAccountBalance(), savingsAccount);
            savingsTransactionRepository.save(savingsTransaction);
            
        } else {
        	
            throw new Exception("Invalid Transfer");
            
        }
        
    }

    public List<Recipient> findRecipientList(User user) {
    	
        String username = user.getUsername();
        List<Recipient> recipientList = recipientRepository.findAll().stream()
                .filter(recipient -> username.equals(recipient.getUser().getUsername()))
                .collect(Collectors.toList());

        return recipientList;
        
    }

    public Recipient saveRecipient(Recipient recipient) {
    	
        return recipientRepository.save(recipient);
        
    }

    public Recipient findRecipientByName(String recipientName) {
    	
        return recipientRepository.findByName(recipientName);
        
    }

    public void deleteRecipientByName(String recipientName) {
    	
        recipientRepository.deleteByName(recipientName);
        
    }

    public void toSomeoneElseTransfer(Recipient recipient, String accountType, String amount, CurrentAccount currentAccount, SavingsAccount savingsAccount) {
    	
        if (accountType.equalsIgnoreCase("Current")) {
            currentAccount.setAccountBalance(currentAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            currentAccountRepository.save(currentAccount);

            Date date = new Date();

            CurrentTransaction currentTransaction = new CurrentTransaction(date, "Transfer to recipient " + recipient.getName(), "Transfer", "Finished", Double.parseDouble(amount), currentAccount.getAccountBalance(), currentAccount);
            currentTransactionRepository.save(currentTransaction);
            
        } else if (accountType.equalsIgnoreCase("Savings")) {
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            savingsAccountRepository.save(savingsAccount);

            Date date = new Date();

            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Transfer to recipient " + recipient.getName(), "Transfer", "Finished", Double.parseDouble(amount), savingsAccount.getAccountBalance(), savingsAccount);
            savingsTransactionRepository.save(savingsTransaction);
            
        }
        
    }

}
