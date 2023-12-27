package com.bankingApplications.dao;

import com.bankingApplications.model.security.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleDao extends CrudRepository<Role, Integer> {
    Role findByName(String name);
}
