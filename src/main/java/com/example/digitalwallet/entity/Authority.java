package com.example.digitalwallet.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter

@Table(name = "ROLE")
public class Authority implements GrantedAuthority {


    @Id
    @Column(name = "ROLE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE_NAME")
    UserRoleName role;
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<User> userList = new HashSet<>();

    @Override
    public String getAuthority() {
        return null;
    }

    public boolean isAdminRole() {
        if (this.role.equals(UserRoleName.ROLE_ADMIN) && this != null) {
            return true;
        }
        return false;
    }
}
