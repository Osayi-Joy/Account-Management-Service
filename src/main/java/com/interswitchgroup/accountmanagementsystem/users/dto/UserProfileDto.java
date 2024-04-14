package com.interswitchgroup.accountmanagementsystem.users.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.interswitchgroup.accountmanagementsystem.common.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author Joy Osayi
 * @createdOn Apr-12(Fri)-2024
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class UserProfileDto {
    @Column(name = "username",unique = true,nullable = false)
    private String username;

    private String password;

    @Column(name = "email",unique = true,nullable = false)
    private String email;

    @Column(name = "first_name",nullable = false)
    private String firstName;

    @Column(name = "last_name",nullable = false)
    private String lastName;

    private String phoneNumber;

    @Column(name = "last_login_date")
    private LocalDateTime lastLoginDate;

    protected LocalDateTime lastModifiedDate;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private Status status = Status.INACTIVE;

}
