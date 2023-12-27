package com.bankingApplications.service.impl;

import com.bankingApplications.dao.*;
import com.bankingApplications.model.*;
import com.bankingApplications.service.TransactionService;
import com.bankingApplications.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private UserService userService;

    @Autowired
    private PrimaryTransactionDao primaryTransactionDao;

    @Autowired
    private SavingsTransactionDao savingsTransactionDao;

    @Autowired
    private PrimaryAccountDao primaryAccountDao;

    @Autowired
    private SavingsAccountDao savingsAccountDao;

    @Autowired
    private RecipientDao recipientDao;

    @Override
    public List<PrimaryTransaction> findPrimaryTransactionList(String username) {
        User user = userService.findByUsername(username);
        return user.getPrimaryAccount().getPrimaryTransactionList();
    }

    @Override
    public List<SavingsTransaction> findSavingsTransactionList(String username) {
        User user = userService.findByUsername(username);
        return user.getSavingsAccount().getSavingsTransactionList();
    }

    @Override
    public void savePrimaryDepositTransaction(PrimaryTransaction primaryTransaction) {
        primaryTransactionDao.save(primaryTransaction);
    }

    @Override
    public void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction) {
        savingsTransactionDao.save(savingsTransaction);
    }

    @Override
    public void savePrimaryWithdrawTransaction(PrimaryTransaction primaryTransaction) {
        primaryTransactionDao.save(primaryTransaction);
    }

    @Override
    public void saveSavingsWithdrawTransaction(SavingsTransaction savingsTransaction) {
        savingsTransactionDao.save(savingsTransaction);
    }

    @Override
    public void betweenAccountsTransfer(String transferFrom, String transferTo, BigDecimal amount, PrimaryAccount primaryAccount, SavingsAccount savingsAccount) throws Exception {
        if (("Primary".equalsIgnoreCase(transferFrom) && "Savings".equalsIgnoreCase(transferTo)) ||
                ("Savings".equalsIgnoreCase(transferFrom) && "Primary".equalsIgnoreCase(transferTo))) {
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(String.valueOf(amount))));
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(String.valueOf(amount))));
            primaryAccountDao.save(primaryAccount);
            savingsAccountDao.save(savingsAccount);

            Date date = new Date();
            if ("Primary".equalsIgnoreCase(transferFrom)) {
                primaryTransactionDao.save(new PrimaryTransaction(date, "Between account transfer from " + transferFrom + " to " + transferTo, "Account", "Finished", amount, primaryAccount.getAccountBalance(), primaryAccount));
            } else {
                savingsTransactionDao.save(new SavingsTransaction(date, "Between account transfer from " + transferFrom + " to " + transferTo, "Transfer", "Finished", amount, savingsAccount.getAccountBalance(), savingsAccount));
            }
        } else {
            throw new Exception("Invalid Transfer");
        }
    }

    @Override
    public List<Recipient> findRecipientList(Principal principal) {
        String username = principal.getName();
        return recipientDao.findAll().stream()
                .filter(recipient -> username.equals(recipient.getUser().getUsername()))
                .collect(Collectors.toList());
    }

    @Override
    public Recipient saveRecipient(Recipient recipient) {
        return recipientDao.save(recipient);
    }

    @Override
    public Recipient findRecipientByName(String recipientName) {
        return recipientDao.findByName(recipientName);
    }

    @Override
    public void deleteRecipientByName(String recipientName) {
        recipientDao.deleteByName(recipientName);
    }

    @Override
    public void toSomeoneElseTransfer(Recipient recipient, String accountType, BigDecimal amount, PrimaryAccount primaryAccount, SavingsAccount savingsAccount) {
        if ("Primary".equalsIgnoreCase(accountType)) {
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(String.valueOf(amount))));
            primaryAccountDao.save(primaryAccount);

            Date date = new Date();
            primaryTransactionDao.save(new PrimaryTransaction(date, "Transfer to recipient " + recipient.getName(), "Transfer", "Finished", amount, primaryAccount.getAccountBalance(), primaryAccount));
        } else if ("Savings".equalsIgnoreCase(accountType)) {
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(String.valueOf(amount))));
            savingsAccountDao.save(savingsAccount);

            Date date = new Date();
            savingsTransactionDao.save(new SavingsTransaction(date, "Transfer to recipient " + recipient.getName(), "Transfer", "Finished", amount, savingsAccount.getAccountBalance(), savingsAccount));
        }
    }
}
