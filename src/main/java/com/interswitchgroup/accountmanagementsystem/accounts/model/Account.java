package com.interswitchgroup.accountmanagementsystem.accounts.model;

import com.interswitchgroup.accountmanagementsystem.accounts.model.enumeration.AccountType;
import com.interswitchgroup.accountmanagementsystem.common.audit.Auditable;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
public class Account extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private String accountNumber;
    private BigDecimal accountBalance = BigDecimal.ZERO;

    public void setId(Long id) {
        this.id = id;
    }

    public Account(Long id, AccountType accountType, String accountNumber, BigDecimal accountBalance, LocalDateTime created_at, LocalDateTime updated_at) {
        this.id = id;
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.accountBalance = accountBalance;
    }

    public Account() {
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) && accountType == account.accountType && Objects.equals(accountNumber, account.accountNumber) && Objects.equals(accountBalance, account.accountBalance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountType, accountNumber, accountBalance);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", accountType=" + accountType +
                ", accountNumber='" + accountNumber + '\'' +
                ", accountBalance=" + accountBalance +
                '}';
    }
}

