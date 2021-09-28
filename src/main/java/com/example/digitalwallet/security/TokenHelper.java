package com.example.digitalwallet.security;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


@Component
@RequiredArgsConstructor


@Slf4j
public class TokenHelper {

    public String getAUTH_HEADER() {
        return AUTH_HEADER;
    }
    public int getEXPIRES_IN() {
        return EXPIRES_IN;
    }
    public String getSECRET() {
        return SECRET;
    }

    @Value("${jwt_header}")
    private String AUTH_HEADER;
    @Value("${expires_in}")
    private int EXPIRES_IN;
    @Value("${secret}")
    public String SECRET;

    //    private TimeProvider
    public String generateToken(String username) {
        LocalDateTime currentTime = LocalDateTime.now();

        return Jwts.builder().setIssuer("spring_API")
                .setSubject(username)
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(currentTime.plusMinutes(EXPIRES_IN).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, SECRET).compact();

    }

    public String refreshToken(String token) {
        String refreshToken;
        LocalDateTime currentTime = LocalDateTime.now();
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            claims
                    .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                    .setExpiration(Date.from(currentTime.plusMinutes(EXPIRES_IN).atZone(ZoneId.systemDefault()).toInstant()));
            refreshToken = Jwts.
                    builder()
                    .setClaims(claims)
                    .setExpiration(Date.from(currentTime
                            .plusMinutes(EXPIRES_IN)
                            .atZone(ZoneId.systemDefault())
                            .toInstant()))
                    .signWith(SignatureAlgorithm.HS512, SECRET)
                    .compact();

            return refreshToken;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Cannot create JWT Token without required fields");

        }
    }

    private Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }


    public String getUsernameFromToken(String token) {
        String username;
        try {
            username = this.getAllClaimsFromToken(token).getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public String getToken(HttpServletRequest request) {
        /**
         *  Getting the token from Authentication header
         *  e.g Bearer your_token
         */
        if (this.getAUTH_HEADER() != null && this.getAUTH_HEADER().startsWith("Barer ")) {
            return this.getAUTH_HEADER().substring(7);
        }
        return null;
    }


    public boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(getSECRET()).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature - {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token - {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token - {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token - {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty - {}", ex.getMessage());
        }
        return false;
    }


    public Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordResetDate) {

        return (lastPasswordResetDate != null && created.before(lastPasswordResetDate));
    }


    public Date getIssuedAtDateFromToken(String token) {
        final Claims claims = this.getAllClaimsFromToken(token);
        Date issueAt = claims.getIssuedAt();
        return issueAt;

    }
}
