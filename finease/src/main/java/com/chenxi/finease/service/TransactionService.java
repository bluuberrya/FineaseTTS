package com.chenxi.finease.service;

import com.chenxi.finease.model.CurrentTransaction;
import com.chenxi.finease.model.SavingsTransaction;

public interface TransactionService {

    // List<CurrentTransaction> findCurrentTransactionList(String username);

    // List<SavingsTransaction> findSavingsTransactionList(String username);

    void saveCurrentDepositTransaction(CurrentTransaction currentTransaction);

    void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction);

    void saveCurrentWithdrawTransaction(CurrentTransaction currentTransaction);

    void saveSavingsWithdrawTransaction(SavingsTransaction savingsTransaction);

    // void betweenAccountsTransfer(String transferFrom, String transferTo, String amount, CurrentAccount currentAccount, SavingsAccount savingsAccount) throws Exception;

    // List<Recipient> findRecipientList(Principal principal);

    // Recipient saveRecipient(Recipient recipient);

    // Recipient findRecipientByName(String recipientName);

    // void deleteRecipientByName(String recipientName);

    // void toSomeoneElseTransfer(Recipient recipient, String accountType, String amount, CurrentAccount currentAccount, SavingsAccount savingsAccount);

}
