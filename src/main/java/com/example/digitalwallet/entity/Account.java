package com.example.digitalwallet.entity;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue
    private Long id;

    private String bankName;
    private BigDecimal balance;
    private int debitCard;
    private String Owner;
    private int CVV;




}
