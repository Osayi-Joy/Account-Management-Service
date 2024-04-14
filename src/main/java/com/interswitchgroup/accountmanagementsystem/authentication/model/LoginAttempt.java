package com.interswitchgroup.accountmanagementsystem.authentication.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.interswitchgroup.accountmanagementsystem.common.audit.Auditable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Joy Osayi
 * @createdOn Apr-13(Sat)-2024
 */
@Entity
@Table(name = "loginAttempts")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@ToString
public class LoginAttempt extends Auditable<String> implements Serializable {
    private String username;
    private int failedAttemptCount;
    private boolean loginAccessDenied;
    private LocalDateTime automatedUnlockTime;
}
