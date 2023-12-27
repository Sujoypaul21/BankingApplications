package com.bankingApplications.controller;

import com.bankingApplications.model.*;
import com.bankingApplications.service.AccountService;
import com.bankingApplications.service.TransactionService;
import com.bankingApplications.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final UserService userService;
    private final AccountService accountService;
    private final TransactionService transactionService;

    @Autowired
    public AccountController(UserService userService, AccountService accountService, TransactionService transactionService) {
        this.userService = userService;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @GetMapping("/primaryAccount")
    public String primaryAccount(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        PrimaryAccount primaryAccount = user.getPrimaryAccount();
        List<PrimaryTransaction> primaryTransactionList = transactionService.findPrimaryTransactionList(principal.getName());

        model.addAttribute("primaryAccount", primaryAccount);
        model.addAttribute("primaryTransactionList", primaryTransactionList);

        return "primaryAccount";
    }

    @GetMapping("/savingsAccount")
    public String savingsAccount(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        SavingsAccount savingsAccount = user.getSavingsAccount();
        List<SavingsTransaction> savingsTransactionList = transactionService.findSavingsTransactionList(principal.getName());

        model.addAttribute("savingsAccount", savingsAccount);
        model.addAttribute("savingsTransactionList", savingsTransactionList);

        return "savingsAccount";
    }

    @GetMapping("/deposit")
    public String deposit(Model model) {
        model.addAttribute("accountType", "");
        model.addAttribute("amount", "");
        return "deposit";
    }

    @PostMapping("/deposit")
    public String depositPost(@RequestParam("amount") BigDecimal amount, @RequestParam("accountType") String accountType, Principal principal) {
        accountService.deposit(accountType, amount, principal);
        return "redirect:/userFront";
    }

    @GetMapping("/withdraw")
    public String withdraw(Model model) {
        model.addAttribute("accountType", "");
        model.addAttribute("amount", "");
        return "withdraw";
    }

    @PostMapping("/withdraw")
    public String withdrawPost(@RequestParam("amount") BigDecimal amount, @RequestParam("accountType") String accountType, Principal principal) {
        accountService.withdraw(accountType, amount, principal);
        return "redirect:/userFronts";
    }
}
