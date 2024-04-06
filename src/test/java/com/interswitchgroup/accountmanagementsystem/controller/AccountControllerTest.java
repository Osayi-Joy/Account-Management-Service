package com.interswitchgroup.accountmanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitchgroup.accountmanagementsystem.dto.AccountBalanceUpdateDto;
import com.interswitchgroup.accountmanagementsystem.dto.AccountDto;
import com.interswitchgroup.accountmanagementsystem.exception.ApplicationException;
import com.interswitchgroup.accountmanagementsystem.exception.NotFoundException;
import com.interswitchgroup.accountmanagementsystem.model.Account;
import com.interswitchgroup.accountmanagementsystem.model.enumeration.AccountType;
import com.interswitchgroup.accountmanagementsystem.model.enumeration.TransactionType;
import com.interswitchgroup.accountmanagementsystem.service.AccountService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class AccountControllerTest {

    @Autowired
    private AccountController accountController;

    @MockBean
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findById() throws Exception {
        Account account = new Account();
        account.setAccountType(AccountType.SAVINGS);
        account.setAccountBalance(BigDecimal.valueOf(500.55));
        account.setId("123456");
        when(accountService.findById("123456")).thenReturn(account);

        this.mockMvc.perform(get("/api/v1/account/123456"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountType", is("SAVINGS")))
                .andExpect(jsonPath("$.accountBalance", is(500.55)))
                .andExpect(jsonPath("$.id", is("123456")));

    }

    @Test
    void findByIdThrowNotFoundException() throws Exception {

        when(accountService.findById("123456")).thenThrow(new NotFoundException("Account with given  id not found"));

        this.mockMvc.perform(get("/api/v1/account/123456"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    assertInstanceOf(NotFoundException.class, result.getResolvedException());
                })
                .andExpect(jsonPath("$.message", is("Account with given  id not found")))
                .andExpect(jsonPath("$.error", is("Not Found")));
    }

    @Test
    void findAllAccount() throws Exception {
        Account account1 = new Account();
        account1.setAccountType(AccountType.SAVINGS);
        Account account2 = new Account();
        account2.setAccountType(AccountType.CURRENT);
        account2.setAccountBalance(BigDecimal.valueOf(300.50));
        when(accountService.findAll()).thenReturn(List.of(account1, account2));

        this.mockMvc.perform(get("/api/v1/account"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].accountType", is("SAVINGS")))
                .andExpect(jsonPath("$[0].accountBalance", is(0)))
                .andExpect(jsonPath("$[1].accountBalance", is(300.50)))
                .andExpect(jsonPath("$[1].accountType", is("CURRENT")));

    }

    @Test
    void createAccount() throws Exception {
        Account account = new Account();
        account.setAccountType(AccountType.SAVINGS);
        account.setAccountBalance(BigDecimal.valueOf(450.55));
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountType(AccountType.SAVINGS);
        when(accountService.createAccount(accountDto)).thenReturn(account);
        String content = "{\"accountType\": \"SAVINGS\"}";
        this.mockMvc.perform(post("/api/v1/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountType", is("SAVINGS")))
                .andExpect(jsonPath("$.accountBalance", is(450.55)));

    }

    @Test
    void updateAccountBalance() throws Exception {
        Account account = new Account();
        account.setId("1234");
        account.setAccountType(AccountType.SAVINGS);
        account.setAccountBalance(BigDecimal.valueOf(200.50));

        AccountBalanceUpdateDto accountBalanceUpdateDto = new AccountBalanceUpdateDto();
        accountBalanceUpdateDto.setAmount(BigDecimal.valueOf(500.00));
        accountBalanceUpdateDto.setTransactionType(TransactionType.DEPOSIT);

        Account updatedAccount = new Account();
        updatedAccount.setId("1234");
        updatedAccount.setAccountType(AccountType.SAVINGS);
        updatedAccount.setAccountBalance(BigDecimal.valueOf(700.50));

        when(accountService.updateAccountBalance("1234", accountBalanceUpdateDto)).thenReturn(updatedAccount);
        when(accountService.findById("1234")).thenReturn(account);

        String content = objectMapper.writeValueAsString(accountBalanceUpdateDto);
        this.mockMvc.perform(patch("/api/v1/account/1234/balance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountType", is("SAVINGS")))
                .andExpect(jsonPath("$.accountBalance", is(700.50)));

        accountBalanceUpdateDto.setTransactionType(TransactionType.WITHDRAW);
        accountBalanceUpdateDto.setAmount(BigDecimal.valueOf(100.00));
        updatedAccount.setAccountBalance(BigDecimal.valueOf(100.50));

        when(accountService.updateAccountBalance("1234", accountBalanceUpdateDto)).thenReturn(updatedAccount);
        when(accountService.findById("1234")).thenReturn(account);

        content = objectMapper.writeValueAsString(accountBalanceUpdateDto);
        this.mockMvc.perform(patch("/api/v1/account/1234/balance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountType", is("SAVINGS")))
                .andExpect(jsonPath("$.accountBalance", is(100.50)));

        accountBalanceUpdateDto.setTransactionType(TransactionType.WITHDRAW);
        accountBalanceUpdateDto.setAmount(BigDecimal.valueOf(300.00));

        when(accountService.updateAccountBalance("1234", accountBalanceUpdateDto)).thenThrow(new ApplicationException("Insufficient balance"));
        when(accountService.findById("1234")).thenReturn(account);

        content = objectMapper.writeValueAsString(accountBalanceUpdateDto);
        this.mockMvc.perform(patch("/api/v1/account/1234/balance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(result -> {
                    assertInstanceOf(ApplicationException.class, result.getResolvedException());
                })
                .andExpect(jsonPath("$.message", is("Insufficient balance")))
                .andExpect(jsonPath("$.error", is("Internal Server Error")));
    }

    @Test
    void updateAccountBalanceConstraintViolation() throws Exception {
        AccountBalanceUpdateDto accountBalanceUpdateDto = new AccountBalanceUpdateDto();
        accountBalanceUpdateDto.setTransactionType(TransactionType.DEPOSIT);
        accountBalanceUpdateDto.setAmount(BigDecimal.valueOf(-300.00));

        when(accountService.updateAccountBalance("1234", accountBalanceUpdateDto)).thenThrow(ConstraintViolationException.class);

        String content = objectMapper.writeValueAsString(accountBalanceUpdateDto);
        this.mockMvc.perform(patch("/api/v1/account/1234/balance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(result -> {
                    assertInstanceOf(ConstraintViolationException.class, result.getResolvedException());
                });
    }

    @Test
    void deleteAccount() throws Exception {
        doNothing().when(accountService).deleteAccount("1234");

        this.mockMvc.perform(delete("/api/v1/account/1234"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}