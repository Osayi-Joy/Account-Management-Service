package com.interswitchgroup.accountmanagementsystem.accounts.repository;

import com.interswitchgroup.accountmanagementsystem.accounts.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByCustomerId(Long customerId);

    @Query("SELECT a FROM Account a WHERE a.accountNumber = :accountNumber OR a.customer.id IN (SELECT a2.customer.id FROM Account a2 WHERE a2.accountNumber = :accountNumber)")
    List<Account> findAllByAccountNumberWithSameCustomerId(@Param("accountNumber") String accountNumber);
}
