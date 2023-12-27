package com.bankingApplications.dao;

import com.bankingApplications.model.SavingsAccount;

import org.springframework.data.repository.CrudRepository;

public interface SavingsAccountDao extends CrudRepository<SavingsAccount, Long> {

    SavingsAccount findByAccountNumber (int accountNumber);
}
