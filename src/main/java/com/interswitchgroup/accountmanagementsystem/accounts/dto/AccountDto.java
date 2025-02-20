package com.interswitchgroup.accountmanagementsystem.accounts.dto;

import com.interswitchgroup.accountmanagementsystem.accounts.model.enumeration.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@Builder
public class AccountDto implements Serializable {
    private AccountType accountType;
    private Long customerId;
}
