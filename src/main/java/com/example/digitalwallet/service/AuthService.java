
package com.example.digitalwallet.service;

import com.example.digitalwallet.dto.security.JwtAuthnticationRequest;
import com.example.digitalwallet.dto.security.RegistrationRequest;
import com.example.digitalwallet.entity.User;
import com.example.digitalwallet.security.TokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;
    private final TokenHelper tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(UserService userService, TokenHelper tokenProvider, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registers a new user in the database by performing a series of quick checks.
     *
     * @return A user object if successfully created
     */
    public Optional<User> registerUser(RegistrationRequest newRegistrationRequest) {
        User newUser = userService.createUser(newRegistrationRequest);
        User registeredNewUser = userService.save(newUser);
        return Optional.ofNullable(registeredNewUser);
    }

    public Optional<Authentication> authenticationUser(JwtAuthnticationRequest loginRequest) {
        return Optional.ofNullable(authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken
                        (loginRequest.getEmail(), loginRequest.getPassword())));
    }


}
