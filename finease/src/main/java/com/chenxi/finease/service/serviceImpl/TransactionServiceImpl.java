package com.chenxi.finease.service.serviceImpl;

import java.math.BigDecimal;
<<<<<<< HEAD
=======
import java.security.Principal;
>>>>>>> FineaseTTS/main
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

<<<<<<< HEAD
=======
import com.chenxi.finease.dao.CurrentAccountDao;
import com.chenxi.finease.dao.CurrentTransactionDao;
import com.chenxi.finease.dao.RecipientDao;
import com.chenxi.finease.dao.SavingsAccountDao;
import com.chenxi.finease.dao.SavingsTransactionDao;
>>>>>>> FineaseTTS/main
import com.chenxi.finease.model.CurrentAccount;
import com.chenxi.finease.model.CurrentTransaction;
import com.chenxi.finease.model.Recipient;
import com.chenxi.finease.model.SavingsAccount;
import com.chenxi.finease.model.SavingsTransaction;
import com.chenxi.finease.model.User;
<<<<<<< HEAD
import com.chenxi.finease.repository.CurrentAccountRepository;
import com.chenxi.finease.repository.CurrentTransactionRepository;
import com.chenxi.finease.repository.RecipientRepository;
import com.chenxi.finease.repository.SavingsAccountRepository;
import com.chenxi.finease.repository.SavingsTransactionRepository;
=======
>>>>>>> FineaseTTS/main
import com.chenxi.finease.service.TransactionService;
import com.chenxi.finease.service.UserService;

@Service
public class TransactionServiceImpl implements TransactionService {
<<<<<<< HEAD
    
=======

>>>>>>> FineaseTTS/main
    @Autowired
    private UserService userService;

    @Autowired
<<<<<<< HEAD
    private CurrentTransactionRepository currentTransactionRepository;

    @Autowired
    private SavingsTransactionRepository savingsTransactionRepository;

    @Autowired
    private CurrentAccountRepository currentAccountRepository;

    @Autowired
    private SavingsAccountRepository savingsAccountRepository;

    @Autowired
    private RecipientRepository recipientRepository;
=======
    private CurrentTransactionDao currentTransactionDao;

    @Autowired
    private SavingsTransactionDao savingsTransactionDao;

    @Autowired
    private CurrentAccountDao currentAccountDao;

    @Autowired
    private SavingsAccountDao savingsAccountDao;

    @Autowired
    private RecipientDao recipientDao;
>>>>>>> FineaseTTS/main


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
    	
<<<<<<< HEAD
        currentTransactionRepository.save(currentTransaction);
=======
        currentTransactionDao.save(currentTransaction);
>>>>>>> FineaseTTS/main
        
    }

    public void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction) {
    	
<<<<<<< HEAD
        savingsTransactionRepository.save(savingsTransaction);
=======
        savingsTransactionDao.save(savingsTransaction);
>>>>>>> FineaseTTS/main
        
    }

    public void saveCurrentWithdrawTransaction(CurrentTransaction currentTransaction) {
    	
<<<<<<< HEAD
        currentTransactionRepository.save(currentTransaction);
=======
        currentTransactionDao.save(currentTransaction);
>>>>>>> FineaseTTS/main
        
    }

    public void saveSavingsWithdrawTransaction(SavingsTransaction savingsTransaction) {
    	
<<<<<<< HEAD
        savingsTransactionRepository.save(savingsTransaction);
=======
        savingsTransactionDao.save(savingsTransaction);
>>>>>>> FineaseTTS/main
        
    }

    public void betweenAccountsTransfer(String transferFrom, String transferTo, String amount, CurrentAccount currentAccount, SavingsAccount savingsAccount) throws Exception {
    	
        if (transferFrom.equalsIgnoreCase("Current") && transferTo.equalsIgnoreCase("Savings")) {
            currentAccount.setAccountBalance(currentAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
<<<<<<< HEAD
            currentAccountRepository.save(currentAccount);
            savingsAccountRepository.save(savingsAccount);
=======
            currentAccountDao.save(currentAccount);
            savingsAccountDao.save(savingsAccount);
>>>>>>> FineaseTTS/main

            Date date = new Date();

            CurrentTransaction currentTransaction = new CurrentTransaction(date, "Between account transfer from " + transferFrom + " to " + transferTo, "Account", "Finished", Double.parseDouble(amount), currentAccount.getAccountBalance(), currentAccount);
<<<<<<< HEAD
            currentTransactionRepository.save(currentTransaction);
=======
            currentTransactionDao.save(currentTransaction);
>>>>>>> FineaseTTS/main
            
        } else if (transferFrom.equalsIgnoreCase("Savings") && transferTo.equalsIgnoreCase("Current")) {
        	
            currentAccount.setAccountBalance(currentAccount.getAccountBalance().add(new BigDecimal(amount)));
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
<<<<<<< HEAD
            currentAccountRepository.save(currentAccount);
            savingsAccountRepository.save(savingsAccount);
=======
            currentAccountDao.save(currentAccount);
            savingsAccountDao.save(savingsAccount);
>>>>>>> FineaseTTS/main

            Date date = new Date();

            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Between account transfer from " + transferFrom + " to " + transferTo, "Transfer", "Finished", Double.parseDouble(amount), savingsAccount.getAccountBalance(), savingsAccount);
<<<<<<< HEAD
            savingsTransactionRepository.save(savingsTransaction);
=======
            savingsTransactionDao.save(savingsTransaction);
>>>>>>> FineaseTTS/main
            
        } else {
        	
            throw new Exception("Invalid Transfer");
            
        }
        
    }

<<<<<<< HEAD
    public List<Recipient> findRecipientList(User user) {
    	
        String username = user.getUsername();
        List<Recipient> recipientList = recipientRepository.findAll().stream()
=======
    public List<Recipient> findRecipientList(Principal principal) {
    	
        String username = principal.getName();
        List<Recipient> recipientList = recipientDao.findAll().stream()
>>>>>>> FineaseTTS/main
                .filter(recipient -> username.equals(recipient.getUser().getUsername()))
                .collect(Collectors.toList());

        return recipientList;
        
    }

    public Recipient saveRecipient(Recipient recipient) {
    	
<<<<<<< HEAD
        return recipientRepository.save(recipient);
=======
        return recipientDao.save(recipient);
>>>>>>> FineaseTTS/main
        
    }

    public Recipient findRecipientByName(String recipientName) {
    	
<<<<<<< HEAD
        return recipientRepository.findByName(recipientName);
=======
        return recipientDao.findByName(recipientName);
>>>>>>> FineaseTTS/main
        
    }

    public void deleteRecipientByName(String recipientName) {
    	
<<<<<<< HEAD
        recipientRepository.deleteByName(recipientName);
=======
        recipientDao.deleteByName(recipientName);
>>>>>>> FineaseTTS/main
        
    }

    public void toSomeoneElseTransfer(Recipient recipient, String accountType, String amount, CurrentAccount currentAccount, SavingsAccount savingsAccount) {
    	
        if (accountType.equalsIgnoreCase("Current")) {
            currentAccount.setAccountBalance(currentAccount.getAccountBalance().subtract(new BigDecimal(amount)));
<<<<<<< HEAD
            currentAccountRepository.save(currentAccount);
=======
            currentAccountDao.save(currentAccount);
>>>>>>> FineaseTTS/main

            Date date = new Date();

            CurrentTransaction currentTransaction = new CurrentTransaction(date, "Transfer to recipient " + recipient.getName(), "Transfer", "Finished", Double.parseDouble(amount), currentAccount.getAccountBalance(), currentAccount);
<<<<<<< HEAD
            currentTransactionRepository.save(currentTransaction);
            
        } else if (accountType.equalsIgnoreCase("Savings")) {
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            savingsAccountRepository.save(savingsAccount);
=======
            currentTransactionDao.save(currentTransaction);
            
        } else if (accountType.equalsIgnoreCase("Savings")) {
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            savingsAccountDao.save(savingsAccount);
>>>>>>> FineaseTTS/main

            Date date = new Date();

            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Transfer to recipient " + recipient.getName(), "Transfer", "Finished", Double.parseDouble(amount), savingsAccount.getAccountBalance(), savingsAccount);
<<<<<<< HEAD
            savingsTransactionRepository.save(savingsTransaction);
=======
            savingsTransactionDao.save(savingsTransaction);
>>>>>>> FineaseTTS/main
            
        }
        
    }
<<<<<<< HEAD

}
=======
    
}
>>>>>>> FineaseTTS/main
