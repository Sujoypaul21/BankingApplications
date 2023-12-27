package com.bankingApplications.service.impl;

import com.bankingApplications.dao.CustomerDao;
import com.bankingApplications.model.Customer;
import com.bankingApplications.service.CustomerService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerRepository;


    @Override
    @Transactional
    public Customer createCustomer(Customer customer) {
        customer.setAccountBalance(new BigDecimal(0.0));
        String accountNumber = generateAccountNumber();
        String pin = generatePin();

        customer.setAccountNumber(accountNumber);
        customer.setAccountPin(pin);

        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> findCustomerList(Principal principal) {
        String username = principal.getName();
        return customerRepository.findAll().stream()
                .filter(customer -> username.equals(customer.getName()))
                .collect(Collectors.toList());
    }

    private String generateAccountNumber() {
        return "ACC" + Math.round(Math.random() * 100000);
    }

    private String generatePin() {
        return String.format("%04d", (int) (Math.random() * 10000));
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public void updateCustomer(String accountNumber, Customer updatedCustomer) {
        Customer existingCustomer = getCustomerByAccountNumber(accountNumber);
        if (existingCustomer != null) {
            existingCustomer.setName(updatedCustomer.getName());
            existingCustomer.setMobileNumber(updatedCustomer.getMobileNumber());
            existingCustomer.setAccountBalance(updatedCustomer.getAccountBalance());
            existingCustomer.setAccountPin(updatedCustomer.getAccountPin());
            existingCustomer.setAddress(updatedCustomer.getAddress());

            customerRepository.save(existingCustomer);
        }
    }
    @Override
    public Customer getCustomerByAccountNumber(String accountNumber) {
        return customerRepository.findAll().stream()
                .filter(customer -> accountNumber.equals(customer.getAccountNumber()))
                .findFirst()
                .orElse(null);
    }
    @Override
    public Customer getCustomerById(Long customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        return optionalCustomer.orElse(null);
    }
    @Override
    public void deleteCustomer(String accountNumber) {
        List<Customer> customers = customerRepository.findAll();
        customers.stream()
                .filter(customer -> accountNumber.equals(customer.getAccountNumber()))
                .findFirst()
                .ifPresent(customerRepository::delete);
    }
}
