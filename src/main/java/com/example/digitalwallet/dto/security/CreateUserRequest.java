package com.example.digitalwallet.dto.security;


import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class CreateUserRequest {

    @NotBlank @Email
    private String username;
    @NotBlank
    private int phone_number;
    @NotBlank
    private String password;


}