package com.interswitchgroup.accountmanagementsystem.authentication.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/**
 * @author Joy Osayi
 * @createdOn Apr-13(Sat)-2024
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class LoginRequestDTO {
    private String username;
    private String password;
}
