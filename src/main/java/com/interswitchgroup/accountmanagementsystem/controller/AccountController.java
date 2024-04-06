package com.interswitchgroup.accountmanagementsystem.controller;

import com.interswitchgroup.accountmanagementsystem.dto.AccountBalanceUpdateDto;
import com.interswitchgroup.accountmanagementsystem.dto.AccountDto;
import com.interswitchgroup.accountmanagementsystem.model.Account;
import com.interswitchgroup.accountmanagementsystem.service.AccountService;
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
    public ResponseEntity<Account> findById(@PathVariable String id) {
        return ResponseEntity.ok().body(accountService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Account>> findAllAccount() {
        return ResponseEntity.ok().body(accountService.findAll());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> createAccount(@RequestBody AccountDto accountDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(accountDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable String id, @RequestBody Account account) {
        return ResponseEntity.ok().body(accountService.updateAccount(id, account));
    }

    @PatchMapping("/{id}/balance")
    public ResponseEntity<Account> updateAccountBalance(@PathVariable String id, @RequestBody AccountBalanceUpdateDto accountBalanceUpdateDto) {
        return ResponseEntity.ok().body(accountService.updateAccountBalance(id, accountBalanceUpdateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable String id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
