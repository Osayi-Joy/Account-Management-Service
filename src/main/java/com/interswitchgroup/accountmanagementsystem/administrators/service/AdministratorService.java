package com.interswitchgroup.accountmanagementsystem.administrators.service;

import com.interswitchgroup.accountmanagementsystem.administrators.dto.AcceptInvitationRequest;
import com.interswitchgroup.accountmanagementsystem.administrators.dto.AdministratorDTO;
import com.interswitchgroup.accountmanagementsystem.common.utils.PaginatedResponseDTO;

/**
 * @author Joy Osayi
 * @createdOn Apr-13(Sat)-2024
 */
public interface AdministratorService {
    void saveSuperAdmin();

    void inviteAdministrator(String email, String role);

    AdministratorDTO acceptInvitation(String invitationCode, AcceptInvitationRequest acceptInvitationRequest);

    PaginatedResponseDTO<AdministratorDTO> getAllAdministrators(int pageNumber, int pageSize);

    AdministratorDTO getAdministratorById(Long administratorId);

    AdministratorDTO viewAuthenticatedAdministratorDetails();
}
