package com.interswitchgroup.accountmanagementsystem.accounts.dto;

import com.interswitchgroup.accountmanagementsystem.accounts.model.enumeration.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Objects;

public class AccountBalanceUpdateDto {
    @NotNull
    @DecimalMin(value = "0.0", message = "amount cannot be negative")
    private BigDecimal amount;
    @NotNull
    private TransactionType transactionType;

    @Override
    public String toString() {
        return "AccountBalanceUpdateDto{" +
                "amount=" + amount +
                ", transactionType=" + transactionType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountBalanceUpdateDto that = (AccountBalanceUpdateDto) o;
        return Objects.equals(amount, that.amount) && transactionType == that.transactionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, transactionType);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}
