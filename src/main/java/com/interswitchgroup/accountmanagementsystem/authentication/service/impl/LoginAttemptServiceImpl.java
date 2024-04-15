package com.interswitchgroup.accountmanagementsystem.authentication.service.impl;

import com.interswitchgroup.accountmanagementsystem.authentication.model.LoginAttempt;
import com.interswitchgroup.accountmanagementsystem.authentication.repository.LoginAttemptRepository;
import com.interswitchgroup.accountmanagementsystem.authentication.service.LoginAttemptService;
import com.interswitchgroup.accountmanagementsystem.common.constants.ErrorConstants;
import com.interswitchgroup.accountmanagementsystem.common.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Joy Osayi
 * @createdOn Apr-13(Sat)-2024
 */
@Service
@RequiredArgsConstructor
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private final LoginAttemptRepository loginAttemptRepository;
    @Value("${account-service.loginAttemptMaxCount}")
    private String loginAttemptMaxCount;

    @Value("${account-service.loginAttemptAutoUnlockDuration}")
    private String loginAttemptAutoUnlockDuration;


    private void systemLockUser(LoginAttempt loginAttempt) {
        loginAttempt.setLoginAccessDenied(true);
        loginAttempt.setAutomatedUnlockTime(
                LocalDateTime.now().plusMinutes(Integer.parseInt(loginAttemptAutoUnlockDuration)));
        this.save(loginAttempt);
    }

    private LoginAttempt getOrCreateByUsername(String username) {
        return this.findByUsername(username)
                .orElseGet(
                        () -> {
                            LoginAttempt loginAttempt = new LoginAttempt();
                            loginAttempt.setFailedAttemptCount(0);
                            loginAttempt.setLoginAccessDenied(false);
                            loginAttempt.setAutomatedUnlockTime(LocalDateTime.now());
                            loginAttempt.setUsername(username);
                            return this.save(loginAttempt);
                        });
    }

    private LoginAttempt save(LoginAttempt userLoginAttempt) {
        return this.loginAttemptRepository.save(userLoginAttempt);
    }

    private Optional<LoginAttempt> findByUsername(String username) {
        return this.loginAttemptRepository.findFirstByUsernameOrderByCreatedDate(username);
    }

    /**
     * Unlock user.
     *
     * @param username the username
     */
    public void unlockUser(String username) {
        LoginAttempt userLoginAttempt = this.getOrCreateByUsername(username);
        this.unlock(userLoginAttempt);
    }

    private void unlock(LoginAttempt userLoginAttempt) {
        userLoginAttempt.setFailedAttemptCount(0);
        userLoginAttempt.setLoginAccessDenied(false);
        userLoginAttempt.setAutomatedUnlockTime(LocalDateTime.now());
        this.save(userLoginAttempt);
    }

    @Override
    public void verifyLoginAccess(String username, boolean credentialMatches) {
        LoginAttempt loginAttempt = this.getOrCreateByUsername(username);
        if (!credentialMatches) {
            if (loginAttempt.isLoginAccessDenied()
                    && this.shouldNotAutomaticallyUnlockProfile(loginAttempt)) {
        throw new BadRequestException(ErrorConstants.LOGIN_ACCESS_DENIED);
            }

            loginAttempt.setFailedAttemptCount(loginAttempt.getFailedAttemptCount() + 1);
            if (loginAttempt.getFailedAttemptCount() >= Integer.parseInt(loginAttemptMaxCount)) {
                this.systemLockUser(loginAttempt);
            }

            this.save(loginAttempt);
        } else {
            if (this.shouldNotAutomaticallyUnlockProfile(loginAttempt)) {
        throw new BadRequestException(ErrorConstants.LOGIN_ACCESS_DENIED);
            }

            this.unlock(loginAttempt);
        }
    }

    private boolean shouldNotAutomaticallyUnlockProfile(LoginAttempt loginAttempt) {
        return !LocalDateTime.now().isAfter(loginAttempt.getAutomatedUnlockTime());
    }
}
