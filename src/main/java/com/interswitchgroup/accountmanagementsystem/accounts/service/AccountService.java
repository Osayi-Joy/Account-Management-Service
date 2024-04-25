package com.interswitchgroup.accountmanagementsystem.accounts.service;

import com.interswitchgroup.accountmanagementsystem.accounts.dto.AccountBalanceUpdateDto;
import com.interswitchgroup.accountmanagementsystem.accounts.dto.AccountDto;
import com.interswitchgroup.accountmanagementsystem.accounts.dto.AccountResponseDto;
import com.interswitchgroup.accountmanagementsystem.common.constants.ErrorConstants;
import com.interswitchgroup.accountmanagementsystem.common.exception.ApplicationException;
import com.interswitchgroup.accountmanagementsystem.common.exception.NotFoundException;
import com.interswitchgroup.accountmanagementsystem.accounts.model.Account;
import com.interswitchgroup.accountmanagementsystem.accounts.model.enumeration.TransactionType;
import com.interswitchgroup.accountmanagementsystem.accounts.repository.AccountRepository;
import com.interswitchgroup.accountmanagementsystem.customers.model.Customer;
import com.interswitchgroup.accountmanagementsystem.customers.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Validated
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;


    public List<AccountResponseDto> findAll() {
        return convertToDto(accountRepository.findAll());
    }

    public AccountResponseDto findById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new NotFoundException("Account with given id not found"));
        return convertToDto(account);
    }

    public List<AccountResponseDto> findByAccountNumber(String accountNumber) {
        return convertToDto(accountRepository.findAllByAccountNumberWithSameCustomerId(accountNumber));
    }
    public AccountResponseDto createAccount(AccountDto accountDto) {
        log.info("Creating account for customer with id: {}", accountDto.getCustomerId());
        Account account1 = new Account();
        Customer customer = customerRepository.findById(accountDto.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorConstants.CUSTOMER_NOT_FOUND));
        account1.setAccountType(accountDto.getAccountType());
        String number = Long.toString((long) (Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L));
        account1.setAccountNumber(number);

        log.info("Account number generated: {}", customer);

        account1.setCustomer(customer);
        return convertToDto(accountRepository.save(account1));
    }

    public AccountResponseDto updateAccount(Long id, Account account) {
        Account account1 = accountRepository.findById(id).orElseThrow(() -> new NotFoundException("Account with given id not found"));
        account1.setAccountType(account.getAccountType());
        return convertToDto(accountRepository.save(account1));
    }

    public AccountResponseDto updateAccountBalance(Long id, @Valid AccountBalanceUpdateDto accountBalanceUpdateDto) {
        Account account1 = accountRepository.findById(id).orElseThrow(() -> new NotFoundException("Account with given id not found"));
        if (accountBalanceUpdateDto.getTransactionType().equals(TransactionType.DEPOSIT)) {
            account1.setAccountBalance(account1.getAccountBalance().add(accountBalanceUpdateDto.getAmount()));
        }
        if (accountBalanceUpdateDto.getTransactionType().equals(TransactionType.WITHDRAW)) {
            if (account1.getAccountBalance().compareTo(accountBalanceUpdateDto.getAmount()) < 0) {
                throw new ApplicationException("Insufficient balance");
            }
            account1.setAccountBalance(account1.getAccountBalance().subtract(accountBalanceUpdateDto.getAmount()));
        }
        return convertToDto(accountRepository.save(account1));
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    public List<AccountResponseDto> getAccountByCustomerId(Long customerId) {
        return convertToDto(accountRepository.findByCustomerId(customerId));
    }

    private AccountResponseDto convertToDto(Account account) {
        return AccountResponseDto.builder()
                .id(account.getId())
                .accountType(account.getAccountType())
                .customerId(account.getCustomer().getId())
                .accountNumber(account.getAccountNumber())
                .accountBalance(account.getAccountBalance())
                .build();
    }

    private List<AccountResponseDto> convertToDto(List<Account> accounts) {
        return accounts.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
