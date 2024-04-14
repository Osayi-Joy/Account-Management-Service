package com.interswitchgroup.accountmanagementsystem.administrators.service;

import com.interswitchgroup.accountmanagementsystem.administrators.model.AdministratorInvitation;
import com.interswitchgroup.accountmanagementsystem.authentication.dto.request.UserAuthProfileRequest;

/**
 * @author Joy Osayi
 * @createdOn Apr-13(Sat)-2024
 */
public interface AdministratorService {
    void saveSuperAdmin();

    AdministratorInvitation inviteAdministrator(String email, String role);

    void acceptInvitation(String invitationCode, UserAuthProfileRequest userProfileRequest);
}
