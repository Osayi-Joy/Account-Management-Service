package com.interswitchgroup.accountmanagementsystem.dto;

import com.interswitchgroup.accountmanagementsystem.model.enumeration.AccountType;

import java.io.Serializable;
import java.util.Objects;

public class AccountDto implements Serializable {
    private AccountType accountType;

    public AccountType getAccountType() {
        return accountType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountDto that = (AccountDto) o;
        return accountType == that.accountType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountType);
    }

    @Override
    public String toString() {
        return "AccountDto{" +
                "accountType=" + accountType +
                '}';
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
