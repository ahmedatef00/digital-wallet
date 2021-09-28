package com.example.digitalwallet.controller;

import com.example.digitalwallet.entity.User;
import com.example.digitalwallet.entity.UserTokenState;
import com.example.digitalwallet.repository.UserRepository;
import com.example.digitalwallet.security.TokenHelper;
import com.example.digitalwallet.dto.security.JwtAuthnticationRequest;
import com.example.digitalwallet.service.CustomUserDetailService;
import com.example.digitalwallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@RestController
@RequestMapping(path = "api/public", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;

    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailService customUserDetailService;
    @Autowired
    TokenHelper tokenHelper;
    @Autowired
    private final UserService userService;
    private UserRepository userRepository;

    public AuthController(PasswordEncoder passwordEncoder, UserService userService, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.userRepository = userRepository;
    }


    //test token test authorization
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String t(HttpServletResponse response) {
        return "response.";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseEntity<UserTokenState> Login(@RequestBody JwtAuthnticationRequest jwtAuthnticationRequest, HttpServletResponse response) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(jwtAuthnticationRequest.getUsername(), jwtAuthnticationRequest.getPassword()));

            User user = (User) authenticate.getPrincipal();

            String token = tokenHelper.generateToken(user.getUsername());

            //return response and put token in header ;
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .body(new UserTokenState(token, tokenHelper.getEXPIRES_IN()));
        } catch (BadCredentialsException ex) {
            System.out.println("FailLogin" + Arrays.stream(ex.getStackTrace()).map(x -> x));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();


        }

    }


    @PostMapping("/register")
    public void signUp(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

}
