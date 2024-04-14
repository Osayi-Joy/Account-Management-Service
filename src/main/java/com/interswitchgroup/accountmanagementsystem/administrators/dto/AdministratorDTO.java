package com.interswitchgroup.accountmanagementsystem.administrators.dto;

import com.interswitchgroup.accountmanagementsystem.common.enums.Status;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Joy Osayi
 * @createdOn Apr-14(Sun)-2024
 */
@Getter
@Setter
public class AdministratorDTO {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String assignedRole;
    private Status status;
}


