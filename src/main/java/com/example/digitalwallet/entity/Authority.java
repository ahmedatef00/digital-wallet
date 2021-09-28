package com.example.digitalwallet.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Getter
@Setter

@Table(name = "roles")
public class Authority implements GrantedAuthority {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    UserRoleName name;

    @Override
    public String getAuthority() {
        return null;
    }


}
