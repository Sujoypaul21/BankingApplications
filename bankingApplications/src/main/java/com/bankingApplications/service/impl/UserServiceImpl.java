package com.bankingApplications.service.impl;

import com.bankingApplications.dao.RoleDao;
import com.bankingApplications.dao.UserDao;
import com.bankingApplications.model.User;
import com.bankingApplications.model.security.UserRole;
import com.bankingApplications.service.AccountService;
import com.bankingApplications.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserDao userDao;
    private final RoleDao roleDao;
//    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, RoleDao roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
//        this.passwordEncoder = passwordEncoder;
    }

    private AccountService accountService;

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void save(User user) {
        userDao.save(user);
    }

    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public User createUser(User user, Set<UserRole> userRoles) {
        if (user == null) {
            LOG.error("User object is null. Cannot create user.");
            return null;
        }

        User localUser = userDao.findByUsername(user.getUsername());

        if (localUser != null) {
            LOG.info("User with username {} already exists. Nothing will be done.", user.getUsername());
        } else {
            String encryptedPassword = user.getPassword();
            user.setPassword(encryptedPassword);

            for (UserRole ur : userRoles) {
                roleDao.save(ur.getRole());
            }

            user.getUserRoles().addAll(userRoles);

            user.setPrimaryAccount(accountService.createPrimaryAccount());
            user.setSavingsAccount(accountService.createSavingsAccount());

            localUser = userDao.save(user);
        }

        return localUser;
    }

    public boolean checkUserExists(String username, String email) {
        return checkUsernameExists(username) || checkEmailExists(email);
    }

    public boolean checkUsernameExists(String username) {
        if (null != findByUsername(username)) {
            return true;
        }

        return false;
    }

    public boolean checkEmailExists(String email) {
        if (null != findByEmail(email)) {
            return true;
        }

        return false;
    }

    public User saveUser (User user) {
        return userDao.save(user);
    }

    public List<User> findUserList() {
        return userDao.findAll();
    }

    public void enableUser (String username) {
        User user = findByUsername(username);
        user.setEnabled(true);
        userDao.save(user);
    }

    public void disableUser (String username) {
        User user = findByUsername(username);
        user.setEnabled(false);
//        System.out.println(u);
        userDao.save(user);
        System.out.println(username + " is disabled.");
    }
}
