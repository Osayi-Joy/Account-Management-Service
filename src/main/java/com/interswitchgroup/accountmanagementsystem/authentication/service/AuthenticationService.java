package com.interswitchgroup.accountmanagementsystem.authentication.service;

import com.interswitchgroup.accountmanagementsystem.authentication.dto.request.LoginRequestDTO;
import com.interswitchgroup.accountmanagementsystem.authentication.dto.response.LoginResponse;

/**
 * @author Joy Osayi
 * @createdOn Apr-13(Sat)-2024
 */
public interface AuthenticationService {
    LoginResponse authenticate(LoginRequestDTO loginRequestDTO);
}
