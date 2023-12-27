package com.bankingApplications.service;

import com.bankingApplications.model.PrimaryAccount;
import com.bankingApplications.model.SavingsAccount;

import java.math.BigDecimal;
import java.security.Principal;

public interface AccountService {

    PrimaryAccount createPrimaryAccount();
    SavingsAccount createSavingsAccount();
    void deposit(String accountType, BigDecimal amount, Principal principal);
    void withdraw(String accountType, BigDecimal amount, Principal principal);
}
