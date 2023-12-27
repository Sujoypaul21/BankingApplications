package com.bankingApplications.service.impl;

import com.bankingApplications.dao.UserDao;
import com.bankingApplications.model.User;
import com.bankingApplications.model.security.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Primary
public class UserSecurityService implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(UserSecurityService.class);

    private final UserDao userDao;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserSecurityService(UserDao userDao, BCryptPasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);

        if (user == null) {
            LOG.warn("Username {} not found", username);
            throw new UsernameNotFoundException("Username " + username + " not found");
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        Set<UserRole> userRoles =user.getUserRoles();
        String userRole ="";
        for(UserRole role :userRoles)
        {
            userRole = role.getRole().getName();
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(encodedPassword)
                .roles(userRole)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

}

