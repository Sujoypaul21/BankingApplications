package com.bankingApplications.controller;

import com.bankingApplications.dao.RoleDao;
import com.bankingApplications.model.PrimaryAccount;
import com.bankingApplications.model.SavingsAccount;
import com.bankingApplications.model.User;
import com.bankingApplications.model.security.UserRole;
import com.bankingApplications.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleDao roleDao;

//    @RequestMapping("/")
//    public String home() {
//        return "redirect:/userFront";
//    }
//
//    @RequestMapping("/index")
//    public String index() {
//        return "userFront";
//    }

//    @RequestMapping(value = "/signup", method = RequestMethod.GET)
//    public String signup(Model model) {
//        User user = new User();
//
//        model.addAttribute("user", user);
//
//        return "signup";
//    }

//    @RequestMapping(value = "/signup", method = RequestMethod.POST)
//    public String signupPost(@ModelAttribute("user") User user,  Model model) {
//
//        if(userService.checkUserExists(user.getUsername(), user.getEmail()))  {
//
//            if (userService.checkEmailExists(user.getEmail())) {
//                model.addAttribute("emailExists", true);
//            }
//
//            if (userService.checkUsernameExists(user.getUsername())) {
//                model.addAttribute("usernameExists", true);
//            }
//
//            return "signup";
//        } else {
//            Set<UserRole> userRoles = new HashSet<>();
//            userRoles.add(new UserRole(user, roleDao.findByName("CUSTOMER")));
//
//            userService.createUser(user, userRoles);
//
//            return "redirect:/";
//        }
//    }

    @RequestMapping("/userFront")
    public String userFront(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        PrimaryAccount primaryAccount = user.getPrimaryAccount();
        SavingsAccount savingsAccount = user.getSavingsAccount();

        model.addAttribute("primaryAccount", primaryAccount);
        model.addAttribute("savingsAccount", savingsAccount);

        return "userFront";
    }
}