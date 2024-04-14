package com.interswitchgroup.accountmanagementsystem.customers.dto;
/**
 * @author Joy Osayi
 * @createdOn Apr-14(Sun)-2024
 */
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CustomerCreationDTO {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;
    private String gender;
}

