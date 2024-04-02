package com.interswitchgroup.accountmanagementsystem.dto;

import com.interswitchgroup.accountmanagementsystem.model.enumeration.AccountType;

public class AccountDto {
    private AccountType accountType;

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
