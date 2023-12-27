package com.bankingApplications.controller;

import com.bankingApplications.model.PrimaryAccount;
import com.bankingApplications.model.Recipient;
import com.bankingApplications.model.SavingsAccount;
import com.bankingApplications.model.User;
import com.bankingApplications.service.TransactionService;
import com.bankingApplications.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/transfer")
public class TransferController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/betweenAccounts", method = RequestMethod.GET)
    public String betweenAccounts(Model model , PrimaryAccount primaryAccount , SavingsAccount savingsAccount) {
        model.addAttribute("transferFrom", "");
        model.addAttribute("transferTo", "");
        model.addAttribute("amount", "");
        BigDecimal primaryBalance = primaryAccount.getAccountBalance();
        BigDecimal savingsBalance = savingsAccount.getAccountBalance();

        // Add dynamic balances to the model
        model.addAttribute("primaryBalance", primaryBalance);
        model.addAttribute("savingsBalance", savingsBalance);

        return "betweenAccounts";
    }

    @RequestMapping(value = "/betweenAccounts", method = RequestMethod.POST)
    public String betweenAccountsPost(
            @ModelAttribute("transferFrom") String transferFrom,
            @ModelAttribute("transferTo") String transferTo,
            @ModelAttribute("amount") BigDecimal amount,
            Principal principal , RedirectAttributes redirectAttributes
    ) throws Exception {
        User user = userService.findByUsername(principal.getName());
        PrimaryAccount primaryAccount = user.getPrimaryAccount();
        SavingsAccount savingsAccount = user.getSavingsAccount();

        if (transferFrom.equals("Primary") && amount.compareTo(primaryAccount.getAccountBalance()) > 0) {
            redirectAttributes.addFlashAttribute("error", "Insufficient funds in the Primary account.");
            return "redirect:/transfer/betweenAccounts";
        }

        if (transferFrom.equals("Savings") && amount.compareTo(savingsAccount.getAccountBalance()) > 0) {
            redirectAttributes.addFlashAttribute("error", "Insufficient funds in the Savings account.");
            return "redirect:/transfer/betweenAccounts";
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            redirectAttributes.addFlashAttribute("error", "Amount must be greater than zero.");
            return "redirect:/transfer/betweenAccounts";
        }

        if (transferFrom.equals("Primary") && primaryAccount.getAccountBalance().compareTo(BigDecimal.ZERO) == 0) {
            redirectAttributes.addFlashAttribute("error", "Primary account has zero balance.");
            return "redirect:/transfer/betweenAccounts";
        }

        if (transferFrom.equals("Savings") && savingsAccount.getAccountBalance().compareTo(BigDecimal.ZERO) == 0) {
            redirectAttributes.addFlashAttribute("error", "Savings account has zero balance.");
            return "redirect:/transfer/betweenAccounts";
        }

        // 2. Check if user submits without any amount
        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            redirectAttributes.addFlashAttribute("error", "Please enter a valid amount.");
            return "redirect:/transfer/betweenAccounts";
        }

        // 3. Check if user added a negative value
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            redirectAttributes.addFlashAttribute("error", "Amount must be greater than zero.");
            return "redirect:/transfer/betweenAccounts";
        }


        transactionService.betweenAccountsTransfer(transferFrom, transferTo, amount, primaryAccount, savingsAccount);

        return "redirect:/userFront";
    }

    @RequestMapping(value = "/recipient", method = RequestMethod.GET)
    public String recipient(Model model, Principal principal) {
        List<Recipient> recipientList = transactionService.findRecipientList(principal);

        Recipient recipient = new Recipient();

        model.addAttribute("recipientList", recipientList);
        model.addAttribute("recipient", recipient);

        return "recipient";
    }

    @RequestMapping(value = "/recipient/save", method = RequestMethod.POST)
    public String recipientPost(@ModelAttribute("recipient") Recipient recipient, Principal principal) {

        User user = userService.findByUsername(principal.getName());
        recipient.setUser(user);
        transactionService.saveRecipient(recipient);

        return "redirect:/transfer/recipient";
    }

    @RequestMapping(value = "/recipient/edit", method = RequestMethod.GET)
    public String recipientEdit(@RequestParam(value = "recipientName") String recipientName, Model model, Principal principal){

        Recipient recipient = transactionService.findRecipientByName(recipientName);
        List<Recipient> recipientList = transactionService.findRecipientList(principal);

        model.addAttribute("recipientList", recipientList);
        model.addAttribute("recipient", recipient);

        return "recipient";
    }

    @RequestMapping(value = "/recipient/delete", method = RequestMethod.GET)
    @Transactional
    public String recipientDelete(@RequestParam(value = "recipientName") String recipientName, Model model, Principal principal){

        transactionService.deleteRecipientByName(recipientName);

        List<Recipient> recipientList = transactionService.findRecipientList(principal);

        Recipient recipient = new Recipient();
        model.addAttribute("recipient", recipient);
        model.addAttribute("recipientList", recipientList);


        return "recipient";
    }

    @RequestMapping(value = "/toSomeoneElse",method = RequestMethod.GET)
    public String toSomeoneElse(Model model, Principal principal) {
        List<Recipient> recipientList = transactionService.findRecipientList(principal);

        model.addAttribute("recipientList", recipientList);
        model.addAttribute("accountType", "");

        return "toSomeoneElse";
    }

    @RequestMapping(value = "/toSomeoneElse",method = RequestMethod.POST)
    public String toSomeoneElsePost(@ModelAttribute("recipientName") String recipientName, @ModelAttribute("accountType") String accountType, @ModelAttribute("amount") BigDecimal amount, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Recipient recipient = transactionService.findRecipientByName(recipientName);
        transactionService.toSomeoneElseTransfer(recipient, accountType, amount, user.getPrimaryAccount(), user.getSavingsAccount());
        return "redirect:/userFront";
    }
}