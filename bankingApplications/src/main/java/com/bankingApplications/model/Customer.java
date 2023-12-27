package com.bankingApplications.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private String mobileNumber;
    private String accountType;
    private BigDecimal accountBalance;
    private String accountNumber;
    private String accountPin;

    public Customer() {
    }

    public Customer(Long id, String name, String address, String mobileNumber, String accountType, BigDecimal accountBalance, String accountNumber, String accountPin) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.mobileNumber = mobileNumber;
        this.accountType = accountType;
        this.accountBalance = accountBalance;
        this.accountNumber = accountNumber;
        this.accountPin = accountPin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountPin() {
        return accountPin;
    }

    public void setAccountPin(String accountPin) {
        this.accountPin = accountPin;
    }
}
