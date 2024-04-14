package com.interswitchgroup.accountmanagementsystem.authentication.service;

import com.interswitchgroup.accountmanagementsystem.authentication.dto.request.UserAuthProfileRequest;
import com.interswitchgroup.accountmanagementsystem.authentication.model.Role;
import com.interswitchgroup.accountmanagementsystem.authentication.model.UserAuthProfile;

/**
 * @author Joy Osayi
 * @createdOn Apr-12(Fri)-2024
 */
public interface UserAuthService {

    UserAuthProfile saveNewUserAuthProfile(UserAuthProfileRequest request);
    boolean doesUserExist(String email);

    void updateUsersPermissions(Role role);

    boolean doesUserExist(String username, String email);
}
