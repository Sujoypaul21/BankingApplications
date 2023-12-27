package com.bankingApplications.controller;

import com.bankingApplications.dao.CustomerDao;
import com.bankingApplications.model.Customer;
import com.bankingApplications.model.Recipient;
import com.bankingApplications.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/portal")
    public String index() {
        return "adminPortal";
    }

    @GetMapping("/save")
    public String createCustomer(Model model) {

        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        return "createBankAccount";
    }

    @PostMapping("/save")
    public String createCustomer(@ModelAttribute("customer") Customer customer) {
        System.out.println("Account Balance before saving: " + customer.getAccountBalance());
        customerService.createCustomer(customer);
        System.out.println("Account Balance After saving: " + customer.getAccountBalance());
        return "redirect:/portal";
    }

    @GetMapping("/list")
    public String getAllCustomers(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "listOfCustomers";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam String accountNumber, Model model) {
        Customer customer = customerService.getCustomerByAccountNumber(accountNumber);
        model.addAttribute("customer", customer);
        return "edit-customer-form";
    }

    @PostMapping("/edit/{accountNumber}")
    public String processEditForm(@PathVariable String accountNumber, @ModelAttribute("customer") Customer updatedCustomer) {
        customerService.updateCustomer(accountNumber, updatedCustomer);
        return "redirect:/list";
    }


    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @Transactional
    public String deleteCustomer(@RequestParam(value = "accountNumber") String accountNumber, Model model, Principal principal) {
        List<Customer> customerList = customerService.findCustomerList(principal);
        customerService.deleteCustomer(accountNumber);
        return "redirect:/portal"; // Redirect to the customer list page after deleting
    }



}
