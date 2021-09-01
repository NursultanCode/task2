package com.bank.testbank.entity;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double balance;
    @OneToOne(mappedBy = "account")
    private User user;
}
