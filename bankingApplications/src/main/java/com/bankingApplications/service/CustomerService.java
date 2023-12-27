package com.bankingApplications.service;

import com.bankingApplications.model.Customer;
import com.bankingApplications.model.Recipient;

import java.security.Principal;
import java.util.List;

public interface CustomerService {

    Customer createCustomer(Customer customer);

    List<Customer> getAllCustomers();

   // Customer updateCustomer(Long customerId, Customer updatedCustomer);

   // void deleteCustomer(Long customerId);

    Customer getCustomerById(Long customerId);

    List<Customer> findCustomerList(Principal principal);

    void deleteCustomer(String accountNumber);

    void updateCustomer(String accountNumber, Customer updatedCustomer);

    Customer getCustomerByAccountNumber(String accountNumber);

}
