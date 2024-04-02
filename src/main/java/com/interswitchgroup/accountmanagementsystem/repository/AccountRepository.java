package com.interswitchgroup.accountmanagementsystem.repository;

import com.interswitchgroup.accountmanagementsystem.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
