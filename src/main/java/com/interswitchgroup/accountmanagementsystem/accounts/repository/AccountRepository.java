package com.interswitchgroup.accountmanagementsystem.accounts.repository;

import com.interswitchgroup.accountmanagementsystem.accounts.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
