package com.interswitchgroup.accountmanagementsystem.accounts.controller;

import com.interswitchgroup.accountmanagementsystem.accounts.dto.AccountBalanceUpdateDto;
import com.interswitchgroup.accountmanagementsystem.accounts.dto.AccountDto;
import com.interswitchgroup.accountmanagementsystem.accounts.dto.AccountResponseDto;
import com.interswitchgroup.accountmanagementsystem.accounts.model.Account;
import com.interswitchgroup.accountmanagementsystem.accounts.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(accountService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDto>> findAllAccount() {
        return ResponseEntity.ok().body(accountService.findAll());
    }

    @GetMapping("/account-number/{accountNumber}") // "findByAccountNumber
    public ResponseEntity<List<AccountResponseDto>> findByAccountNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok().body(accountService.findByAccountNumber(accountNumber));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponseDto> createAccount(@RequestBody AccountDto accountDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(accountDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDto> updateAccount(@PathVariable Long id, @RequestBody Account account) {
        return ResponseEntity.ok().body(accountService.updateAccount(id, account));
    }

    @PatchMapping("/{id}/balance")
    public ResponseEntity<AccountResponseDto> updateAccountBalance(@PathVariable Long id, @RequestBody AccountBalanceUpdateDto accountBalanceUpdateDto) {
        return ResponseEntity.ok().body(accountService.updateAccountBalance(id, accountBalanceUpdateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
