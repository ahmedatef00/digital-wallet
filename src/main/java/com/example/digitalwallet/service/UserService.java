package com.example.digitalwallet.service;

import com.example.digitalwallet.dto.security.RegistrationRequest;
import com.example.digitalwallet.entity.Authority;
import com.example.digitalwallet.entity.User;
import com.example.digitalwallet.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Data
@Service
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleservice;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleservice) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleservice = roleservice;
    }

    /**
     * Save the user to the database
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    //GET List OF ALl Users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    //Get single user by id
    public User getUserById(Long id) {
        return userRepository.getById(id);
    }


    public User createUser(RegistrationRequest registerRequest) {

        User newUser = new User();
        Boolean isNewUserAsAdmin = registerRequest.getRegisterAsAdmin();
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setUsername(registerRequest.getUsername());
        newUser.addRoles(getRolesForNewUser(isNewUserAsAdmin));
        newUser.setActive(true);
        newUser.setIsEmailVerified(false);
        return newUser;

    }

    private Set<Authority> getRolesForNewUser(Boolean isToBeAdmin) {

        Set<Authority> newUserRoleSet = new HashSet<>(roleservice.findAll());
        if (!isToBeAdmin) {
            newUserRoleSet.removeIf(Authority::isAdminRole);
        }
        return newUserRoleSet;
    }
}
