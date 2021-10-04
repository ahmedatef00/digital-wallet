package com.example.digitalwallet.config;

import com.example.digitalwallet.repository.UserRepository;
import com.example.digitalwallet.security.TokenHelper;
import com.example.digitalwallet.security.auth.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;

import static java.lang.String.format;

/**
 * The server-side authentication configurations
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
/***
 * securedEnabled = true enables @Secured annotation.
 * jsr250Enabled = true enables @RolesAllowed annotation.
 * prePostEnabled = true enables @PreAuthorize, @PostAuthorize, @PreFilter, @PostFilter annotations.
 */

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    public WebSecurityConfig(JwtTokenFilter jwtTokenFilter, UserRepository userRepository, TokenHelper tokenHelper) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.userRepository = userRepository;
        this.tokenHelper = tokenHelper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    private final JwtTokenFilter jwtTokenFilter;


    private final UserRepository userRepository;

    private final TokenHelper tokenHelper;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Enable CORS and disable CSRF
        http = http.cors().and().csrf().disable();

        //enable h2 database
        http.headers().frameOptions().disable();

        // Set session management to stateless
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        // Set unauthorized requests exception handler
        http = http
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            response.sendError(
                                    HttpServletResponse.SC_UNAUTHORIZED,
                                    ex.getMessage()
                            );
                        }
                )
                .and();
        // Set permissions on endpoints
        http.authorizeRequests()
                // Swagger endpoints must be publicly accessible
                .antMatchers("/").permitAll()
                .antMatchers(format("%s/**", "/rest-api-docs")).permitAll()
                .antMatchers(format("%s/**", "/swagger-ui")).permitAll()
                .antMatchers(format("%s/**", "/h2-console")).permitAll()
                // Our public endpoints
                .antMatchers("/api/public/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/author/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/author/search").permitAll()
                .antMatchers(HttpMethod.GET, "/api/book/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/book/search").permitAll()
                // Our private endpoints

                .anyRequest().authenticated();

        // Add JWT token filter
        http.addFilterBefore(
                jwtTokenFilter,
                UsernamePasswordAuthenticationFilter.class
        );


    }

    // Used by spring security if CORS is enabled.
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username -> userRepository
                .findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                format("User: %s, not found", username)
                        )
                ));

    }
}
