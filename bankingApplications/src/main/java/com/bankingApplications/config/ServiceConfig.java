package com.bankingApplications.config;

import com.bankingApplications.service.TransactionService;

import com.bankingApplications.service.impl.TransactionServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ServiceConfig {

    @Bean
    public TransactionService transactionService() {
        return new TransactionServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // Other service beans can be defined here...
}

