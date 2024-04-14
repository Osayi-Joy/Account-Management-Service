package com.interswitchgroup.accountmanagementsystem.users.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.interswitchgroup.accountmanagementsystem.common.audit.Auditable;
import com.interswitchgroup.accountmanagementsystem.common.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Joy Osayi
 * @createdOn Apr-12(Fri)-2024
 */
@MappedSuperclass
@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile extends Auditable<String> implements Serializable {

    @Column(name = "username",unique = true,nullable = false)
    private String username;

    private String password;

    @Column(name = "email",unique = true,nullable = false)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String phoneNumber;

    @Column(name = "last_login_date")
    private LocalDateTime lastLoginDate;

}
