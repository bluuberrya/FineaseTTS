package com.chenxi.finease.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.chenxi.finease.model.CurrentAccount;
import com.chenxi.finease.model.CurrentTransaction;
import com.chenxi.finease.model.SavingsAccount;
import com.chenxi.finease.model.SavingsTransaction;
import com.chenxi.finease.model.User;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;

public interface TransactionService {

    List<CurrentTransaction> findCurrentTransactionList(String username);

    List<SavingsTransaction> findSavingsTransactionList(String username);

    List<CurrentTransaction> findAllCurrentTransactionList();

    List<SavingsTransaction> findAllSavingsTransactionList();

    void saveCurrentDepositTransaction(CurrentTransaction currentTransaction);

    void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction);

    void saveCurrentWithdrawTransaction(CurrentTransaction currentTransaction);

    void saveSavingsWithdrawTransaction(SavingsTransaction savingsTransaction);

    void toSomeoneElseTransfer(User transferTo, String transferFrom, String amount, CurrentAccount currentAccount, SavingsAccount savingsAccount);

    void generateCurrentReceipt(CurrentTransaction transaction) throws FileNotFoundException, DocumentException, IOException;

    void generateSavingsReceipt(SavingsTransaction transaction) throws FileNotFoundException, DocumentException, IOException;

    String getCFileName(CurrentTransaction transaction);

    String getSFileName(SavingsTransaction transaction);

    void addCTransactionDetails(Document document, CurrentTransaction transaction) throws DocumentException;
    
    void addSTransactionDetails(Document document, SavingsTransaction transaction) throws DocumentException;
}
