package com.example.digitalwallet.entity;

import com.example.digitalwallet.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(min = 4, max = 255, message = "Minimum username length: 4 characters")
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    //    @Size(min = 10, max = 10)
    private int phone_number;


    @NotNull
    @Size(min = 8, message = "Minimum password length: 8 characters")
    private String password;


    //    @Email(regexp = "[0-9a-z]+\\@[0-9a-z]+\\.com\\.br")
    private String email;
    //    @Size(min = 10, max = 99)
    private int age;
    //    @NotNull
    private Gender gender;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "last_password_reset_date")
    private Timestamp lastPasswordResetDate;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),//  is the current table
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    // inverse is the second table
    private List<Authority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }


    @Override
    @JsonIgnore

    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore

    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore

    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
