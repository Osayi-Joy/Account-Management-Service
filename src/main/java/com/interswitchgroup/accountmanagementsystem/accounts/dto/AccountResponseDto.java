package com.interswitchgroup.accountmanagementsystem.accounts.dto;

import com.interswitchgroup.accountmanagementsystem.accounts.model.enumeration.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@Builder
public class AccountResponseDto implements Serializable {
    private Long id;
    private AccountType accountType;
    private Long customerId;
    private String accountNumber;
    private BigDecimal accountBalance;
}
