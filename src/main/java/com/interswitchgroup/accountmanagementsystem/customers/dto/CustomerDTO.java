package com.interswitchgroup.accountmanagementsystem.customers.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author Joy Osayi
 * @createdOn Apr-14(Sun)-2024
 */
@Getter
@Setter
public class CustomerDTO {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;
    private String gender;

}
