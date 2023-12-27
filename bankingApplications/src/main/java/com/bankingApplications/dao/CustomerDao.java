package com.bankingApplications.dao;

import com.bankingApplications.model.Customer;
import com.bankingApplications.model.Recipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerDao extends JpaRepository<Customer, Long> {

    List<Customer> findAll();
}
