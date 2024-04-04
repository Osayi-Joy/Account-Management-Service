package com.interswitchgroup.accountmanagementsystem.model;

import com.interswitchgroup.accountmanagementsystem.audit.DateAudit;
import com.interswitchgroup.accountmanagementsystem.model.enumeration.AccountType;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Account extends DateAudit {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private String accountNumber;
    private BigDecimal accountBalance = BigDecimal.ZERO;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Account(String id, AccountType accountType, String accountNumber, BigDecimal accountBalance, LocalDateTime created_at, LocalDateTime updated_at) {
        this.id = id;
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.accountBalance = accountBalance;
    }

    public Account() {
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
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

