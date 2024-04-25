package com.interswitchgroup.accountmanagementsystem.accounts.model;

import com.interswitchgroup.accountmanagementsystem.accounts.model.enumeration.AccountType;
import com.interswitchgroup.accountmanagementsystem.common.audit.Auditable;
import com.interswitchgroup.accountmanagementsystem.customers.model.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private String accountNumber;
    private BigDecimal accountBalance = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}

