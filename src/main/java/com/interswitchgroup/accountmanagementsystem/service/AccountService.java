package com.interswitchgroup.accountmanagementsystem.service;

import com.interswitchgroup.accountmanagementsystem.dto.AccountDto;
import com.interswitchgroup.accountmanagementsystem.exception.NotFoundException;
import com.interswitchgroup.accountmanagementsystem.model.Account;
import com.interswitchgroup.accountmanagementsystem.model.enumeration.AccountType;
import com.interswitchgroup.accountmanagementsystem.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account findById(String id) {
        return accountRepository.findById(id).orElseThrow(() -> new NotFoundException("Account with given  id not found"));
    }

    public Account createAccount(AccountDto accountDto) {
        Account account1 = new Account();
        account1.setAccountType(accountDto.getAccountType());
        String number = Long.toString((long) (Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L));
        account1.setAccountNumber(number);
        return accountRepository.save(account1);
    }

    public Account updateAccount(String id, Account account) {
        Account account1 = accountRepository.findById(id).orElseThrow(() -> new NotFoundException("Account with given id not found"));
        account1.setAccountBalance(account.getAccountBalance().add(account1.getAccountBalance()));
        return accountRepository.save(account1);
    }

    public void deleteAccount(String id) {
        accountRepository.deleteById(id);
    }
}
