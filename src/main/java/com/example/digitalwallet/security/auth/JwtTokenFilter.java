package com.example.digitalwallet.security.auth;


import com.example.digitalwallet.repository.UserRepository;
import com.example.digitalwallet.security.TokenHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static java.util.List.of;
import static java.util.Optional.ofNullable;


@Component

public class JwtTokenFilter extends OncePerRequestFilter {

    UserRepository userRepo;
    private TokenHelper tokenHelper;

    public JwtTokenFilter(TokenHelper tokenHelper) {
        this.tokenHelper = tokenHelper;
    }

    @Override
    public void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        String token = tokenHelper.getToken(request);
        if (!tokenHelper.validate(token)) {
            System.out.println(tokenHelper.validate(token) + "WTF");

            chain.doFilter(request, response);
            return;

        }
        // Get user identity and set it on the spring security context
        UserDetails userDetails = userRepo
                .findByUsername(tokenHelper.getUsernameFromToken(token))
                .orElse(null);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null,
                userDetails == null ?
                        List.of() : userDetails.getAuthorities()
        );

        authentication
                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);


    }

}