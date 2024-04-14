package com.interswitchgroup.accountmanagementsystem.common.utils.response;

import lombok.*;

/**
 * @author Joy Osayi
 * @createdOn April-12(Fri)-2024
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApiError {
    public ApiError(String message) {
        this.message = message;
    }

    public static final String ERROR_UNKNOWN = "90";

    private String message;
    private String code;
}
