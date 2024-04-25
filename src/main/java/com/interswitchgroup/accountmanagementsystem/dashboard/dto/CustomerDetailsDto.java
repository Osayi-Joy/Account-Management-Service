package com.interswitchgroup.accountmanagementsystem.dashboard.dto;

import com.interswitchgroup.accountmanagementsystem.accounts.dto.AccountResponseDto;
import com.interswitchgroup.accountmanagementsystem.customers.dto.CustomerDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDetailsDto implements Serializable {
    private List<AccountResponseDto> accounts;
    private CustomerDTO customer;
}
