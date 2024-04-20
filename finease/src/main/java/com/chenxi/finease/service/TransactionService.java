package com.chenxi.finease.service;

<<<<<<< HEAD
import com.chenxi.finease.model.CurrentTransaction;
=======
import java.security.Principal;
import java.util.List;

import com.chenxi.finease.model.CurrentAccount;
import com.chenxi.finease.model.CurrentTransaction;
import com.chenxi.finease.model.Recipient;
import com.chenxi.finease.model.SavingsAccount;
>>>>>>> FineaseTTS/main
import com.chenxi.finease.model.SavingsTransaction;

public interface TransactionService {

<<<<<<< HEAD
    // List<CurrentTransaction> findCurrentTransactionList(String username);

    // List<SavingsTransaction> findSavingsTransactionList(String username);
=======
    List<CurrentTransaction> findCurrentTransactionList(String username);

    List<SavingsTransaction> findSavingsTransactionList(String username);
>>>>>>> FineaseTTS/main

    void saveCurrentDepositTransaction(CurrentTransaction currentTransaction);

    void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction);

    void saveCurrentWithdrawTransaction(CurrentTransaction currentTransaction);

    void saveSavingsWithdrawTransaction(SavingsTransaction savingsTransaction);

<<<<<<< HEAD
    // void betweenAccountsTransfer(String transferFrom, String transferTo, String amount, CurrentAccount currentAccount, SavingsAccount savingsAccount) throws Exception;

    // List<Recipient> findRecipientList(Principal principal);

    // Recipient saveRecipient(Recipient recipient);

    // Recipient findRecipientByName(String recipientName);

    // void deleteRecipientByName(String recipientName);

    // void toSomeoneElseTransfer(Recipient recipient, String accountType, String amount, CurrentAccount currentAccount, SavingsAccount savingsAccount);

}
=======
    void betweenAccountsTransfer(String transferFrom, String transferTo, String amount, CurrentAccount currentAccount, SavingsAccount savingsAccount) throws Exception;

    List<Recipient> findRecipientList(Principal principal);

    Recipient saveRecipient(Recipient recipient);

    Recipient findRecipientByName(String recipientName);

    void deleteRecipientByName(String recipientName);

    void toSomeoneElseTransfer(Recipient recipient, String accountType, String amount, CurrentAccount currentAccount, SavingsAccount savingsAccount);

}
>>>>>>> FineaseTTS/main
