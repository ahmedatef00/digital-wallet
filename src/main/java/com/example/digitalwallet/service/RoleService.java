
package com.example.digitalwallet.service;

import com.example.digitalwallet.entity.Authority;
import com.example.digitalwallet.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Find all roles from the database
     */
    public Collection<Authority> findAll() {
        return roleRepository.findAll();
    }

}
