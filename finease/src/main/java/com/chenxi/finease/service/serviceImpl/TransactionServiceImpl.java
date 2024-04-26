package com.chenxi.finease.service.serviceImpl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.chenxi.finease.model.CurrentTransaction;
import com.chenxi.finease.model.SavingsTransaction;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chenxi.finease.model.CurrentAccount;
import com.chenxi.finease.model.SavingsAccount;
import com.chenxi.finease.model.User;
import com.chenxi.finease.repository.CurrentAccountRepository;
import com.chenxi.finease.repository.CurrentTransactionRepository;
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

    public List<SavingsTransaction> findAllSavingsTransactionList() {
    	
        return savingsTransactionRepository.findAll();
        
    }

    public List<CurrentTransaction> findAllCurrentTransactionList() {
    	
        return currentTransactionRepository.findAll();
        
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
    
    public void toSomeoneElseTransfer(User transferTo, String transferFrom, String amount, CurrentAccount currentAccount, SavingsAccount savingsAccount) {
        try {
            BigDecimal transferAmount = new BigDecimal(amount);
            if (transferFrom.equalsIgnoreCase("Current")) {
                currentAccount.setAccountBalance(currentAccount.getAccountBalance().subtract(transferAmount));
                currentAccountRepository.save(currentAccount);
    
                User transfrom = userService.findByUserId(currentAccount.getId());
                CurrentAccount recipientCurrentAccount = transferTo.getCurrentAccount();
                recipientCurrentAccount.setAccountBalance(recipientCurrentAccount.getAccountBalance().add(transferAmount));
                currentAccountRepository.save(recipientCurrentAccount);
    
                Date date = new Date();
                CurrentTransaction currentTransaction = new CurrentTransaction(date, transfrom.getUsername() + " transfer to " + transferTo.getUsername(), "Transfer", "Finished", transferAmount.doubleValue(), currentAccount.getAccountBalance(), currentAccount);
                currentTransactionRepository.save(currentTransaction);
    
                generateCurrentReceipt(currentTransaction);
            } else if (transferFrom.equalsIgnoreCase("Savings")) {
                savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(transferAmount));
                savingsAccountRepository.save(savingsAccount);
    
                User transfrom = userService.findByUserId(currentAccount.getId());
                SavingsAccount recipientSavingsAccount = transferTo.getSavingsAccount();
                recipientSavingsAccount.setAccountBalance(recipientSavingsAccount.getAccountBalance().add(transferAmount));
                savingsAccountRepository.save(recipientSavingsAccount);
    
                Date date = new Date();
                SavingsTransaction savingsTransaction = new SavingsTransaction(date, transfrom.getUsername() + " transfer to " + transferTo.getUsername(), "Transfer", "Finished", transferAmount.doubleValue(), savingsAccount.getAccountBalance(), savingsAccount);
                savingsTransactionRepository.save(savingsTransaction);
    
                generateSavingsReceipt(savingsTransaction);
            }
        } catch (Exception e) {
            System.err.println("Error occurred during transaction: " + e.getMessage());
            e.printStackTrace();
        }
    }    


    //PDF
    public void generateCurrentReceipt(CurrentTransaction transaction) throws DocumentException, IOException {
        Document document = new Document();
        OutputStream outputStream = new FileOutputStream("D:/APU/a_FYP/PROJECT/finease_a/finease/src/main/resources/static/pdf/" + getCFileName(transaction));
        PdfWriter.getInstance(document, outputStream);
        document.open();
        addCTransactionDetails(document, transaction);
        document.close();
        outputStream.close(); // Close the OutputStream
    }

    public void generateSavingsReceipt(SavingsTransaction transaction) throws DocumentException, IOException {
        Document document = new Document();
        OutputStream outputStream = new FileOutputStream("D:/APU/a_FYP/PROJECT/finease_a/finease/src/main/resources/static/pdf/" + getSFileName(transaction));
        PdfWriter.getInstance(document, outputStream);
        document.open();
        addSTransactionDetails(document, transaction);
        document.close();
        outputStream.close(); // Close the OutputStream
    }

    public String getCFileName(CurrentTransaction transaction) {
        return "CurrentTransaction-" + transaction.getId() + ".pdf";
    }

    public String getSFileName(SavingsTransaction transaction) {
        return "SavingsTransaction-" + transaction.getId() + ".pdf";
    }

    public void addCTransactionDetails(Document document, CurrentTransaction transaction) throws DocumentException {
        addCTransactionDetails(document, transaction.getId(), transaction.getCurrentAccount(), transaction.getDate(), transaction.getType(), transaction.getAmount(), transaction.getDescription());
    }

    public void addSTransactionDetails(Document document, SavingsTransaction transaction) throws DocumentException {
        addSTransactionDetails(document, transaction.getId(), transaction.getSavingsAccount(), transaction.getDate(), transaction.getType(), transaction.getAmount(), transaction.getDescription());
    }

    public void addCTransactionDetails(Document document, Long id, CurrentAccount account, Date date, String type, double amount, String description) throws DocumentException {
        document.add(new Paragraph("----------------------------------------------------------------------------"));
        document.add(new Paragraph("                               BANKING RECEIPT                     "));
        document.add(new Paragraph("----------------------------------------------------------------------------"));
        document.add(new Paragraph("  Date                          : " + date));
        document.add(new Paragraph("  Account Number         : " + account.getAccountNumber()));
        document.add(new Paragraph("  Transaction Type         : " + type));
        document.add(new Paragraph("  Amount                       : " + amount));
        document.add(new Paragraph("  Description               : " + description));
        document.add(new Paragraph("  Balance After           : " + account.getAccountBalance()));
        document.add(new Paragraph("  Transaction ID            : " + id));
        document.add(new Paragraph("----------------------------------------------------------------------------"));
        document.add(new Paragraph("  Thank you for banking with us!"));
        document.add(new Paragraph("  For inquiries, contact us at customer@finease.com"));
        document.add(new Paragraph("----------------------------------------------------------------------------"));
    
    }

    public void addSTransactionDetails(Document document, Long id, SavingsAccount account, Date date, String type, double amount, String description) throws DocumentException {
        document.add(new Paragraph("-------------------------------------------------------------------------------"));
        document.add(new Paragraph("                                 BANKING RECEIPT                     "));
        document.add(new Paragraph("-------------------------------------------------------------------------------"));
        document.add(new Paragraph("  Date                             : " + date));
        document.add(new Paragraph("  Account Number          : " + account.getAccountNumber()));
        document.add(new Paragraph("  Transaction Type         : " + type));
        document.add(new Paragraph("  Amount                         : " + amount));
        document.add(new Paragraph("  Description                   : " + description));
        document.add(new Paragraph("  Balance After               : " + account.getAccountBalance()));
        document.add(new Paragraph("  Transaction ID             : " + id));
        document.add(new Paragraph("-------------------------------------------------------------------------------"));
        document.add(new Paragraph("  Thank you for banking with us!"));
        document.add(new Paragraph("  For inquiries, contact us at customer@finease.com"));
        document.add(new Paragraph("-------------------------------------------------------------------------------"));
    
    }

    @Override
    public int getTotalNumberOfCurrentTransactions() {
        return (int) currentTransactionRepository.count();
    }

    @Override
    public int getTotalNumberOfSavingsTransactions() {
        return (int) savingsTransactionRepository.count();
    }

    @Override
    public int getTotalNumberOfDeposits() {
        List<CurrentTransaction> currentTransactions = currentTransactionRepository.findAll();
        List<SavingsTransaction> savingsTransactions = savingsTransactionRepository.findAll();

        int depositCount = 0;

        // Count deposits in current transactions
        for (CurrentTransaction transaction : currentTransactions) {
            if (transaction.getDescription().toLowerCase().contains("deposit")) {
                depositCount++;
            }
        }

        // Count deposits in savings transactions
        for (SavingsTransaction transaction : savingsTransactions) {
            if (transaction.getDescription().toLowerCase().contains("deposit")) {
                depositCount++;
            }
        }

        return depositCount;
    }

    @Override
    public int getTotalNumberOfDWithdraws() {
        List<CurrentTransaction> currentTransactions = currentTransactionRepository.findAll();
        List<SavingsTransaction> savingsTransactions = savingsTransactionRepository.findAll();

        int withdrawCount = 0;

        // Count withdrawals in current transactions
        for (CurrentTransaction transaction : currentTransactions) {
            if (transaction.getDescription().toLowerCase().contains("withdraw")) {
                withdrawCount++;
            }
        }

        // Count withdrawals in savings transactions
        for (SavingsTransaction transaction : savingsTransactions) {
            if (transaction.getDescription().toLowerCase().contains("withdraw")) {
                withdrawCount++;
            }
        }

        return withdrawCount;
    }

    @Override
    public int getTotalNumberOfTransfers() {
        List<CurrentTransaction> currentTransactions = currentTransactionRepository.findAll();
        List<SavingsTransaction> savingsTransactions = savingsTransactionRepository.findAll();

        int transferCount = 0;

        // Count transfers in current transactions
        for (CurrentTransaction transaction : currentTransactions) {
            if (transaction.getDescription().toLowerCase().contains("transfer")) {
                transferCount++;
            }
        }

        // Count transfers in savings transactions
        for (SavingsTransaction transaction : savingsTransactions) {
            if (transaction.getDescription().toLowerCase().contains("transfer")) {
                transferCount++;
            }
        }

        return transferCount;
    }

}