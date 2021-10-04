package com.example.digitalwallet.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.*;
import java.util.Date;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@RedisHash("transaction")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private float amount;
    private float postBalance;
    private Date timestamp;
    private String description;

    @Indexed
    private Long transactionFromAccount;
    @Indexed
    private Long transactionToAccount;

}
