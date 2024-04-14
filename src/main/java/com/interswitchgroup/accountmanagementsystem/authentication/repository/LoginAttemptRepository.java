package com.interswitchgroup.accountmanagementsystem.authentication.repository;

import com.interswitchgroup.accountmanagementsystem.authentication.model.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Joy Osayi
 * @createdOn Apr-13(Sat)-2024
 */
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {
    Optional<LoginAttempt> findFirstByUsernameOrderByCreatedDate(String username);
}
