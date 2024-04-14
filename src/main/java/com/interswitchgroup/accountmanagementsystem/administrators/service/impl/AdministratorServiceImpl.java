package com.interswitchgroup.accountmanagementsystem.administrators.service.impl;

import static com.interswitchgroup.accountmanagementsystem.common.constants.Constants.*;

import com.interswitchgroup.accountmanagementsystem.administrators.model.Administrator;
import com.interswitchgroup.accountmanagementsystem.administrators.model.AdministratorInvitation;
import com.interswitchgroup.accountmanagementsystem.administrators.repository.AdministratorInvitationRepository;
import com.interswitchgroup.accountmanagementsystem.administrators.repository.AdministratorRepository;
import com.interswitchgroup.accountmanagementsystem.administrators.service.AdministratorService;
import com.interswitchgroup.accountmanagementsystem.authentication.dto.request.UserAuthProfileRequest;
import com.interswitchgroup.accountmanagementsystem.authentication.model.UserAuthProfile;
import com.interswitchgroup.accountmanagementsystem.authentication.service.RoleService;
import com.interswitchgroup.accountmanagementsystem.authentication.service.UserAuthService;
import com.interswitchgroup.accountmanagementsystem.common.constants.ErrorConstants;
import com.interswitchgroup.accountmanagementsystem.common.exception.BadRequestException;
import com.interswitchgroup.accountmanagementsystem.common.exception.NotFoundException;
import jakarta.persistence.EntityExistsException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.interswitchgroup.accountmanagementsystem.common.enums.Status;

/**
 * @author Joy Osayi
 * @createdOn Apr-12(Fri)-2024
 */

@Service
@RequiredArgsConstructor
public class AdministratorServiceImpl implements AdministratorService {
    private final UserAuthService userAuthServiceImpl;
    private final AdministratorRepository administratorRepository;
    private final AdministratorInvitationRepository administratorInvitationRepository;
    private final RoleService roleServiceImpl;
    @Override
    public void saveSuperAdmin() {
        if (administratorRepository.existsByUserAuthProfile_Email(SUPER_ADMIN_EMAIL)) {
            return;
        }
        UserAuthProfileRequest superAdminRequest = new UserAuthProfileRequest();
        superAdminRequest.setUsername(SUPER_ADMIN_USERNAME);
        superAdminRequest.setPassword(SUPER_ADMIN_PASSWORD);
        superAdminRequest.setEmail(SUPER_ADMIN_EMAIL);
        superAdminRequest.setFirstName(SUPER_ADMIN_FIRSTNAME);
        superAdminRequest.setLastName(SUPER_ADMIN_LASTNAME);
        superAdminRequest.setAssignedRole(ROLE_SUPER_ADMIN);

        UserAuthProfile userAuthProfile = userAuthServiceImpl.saveNewUserAuthProfile(superAdminRequest);

        Administrator superAdmin =  createAdministrator(userAuthProfile);

        administratorRepository.save(superAdmin);
    }

    @Override
    public AdministratorInvitation inviteAdministrator(String email, String role) {
        roleServiceImpl.validateAdministratorRole(role);

        if (administratorRepository.existsByUserAuthProfile_Email(email)) {
            throw new EntityExistsException(ErrorConstants.ADMIN_EXISTS_EMAIL);
        }

        Optional<AdministratorInvitation> invitationOptional = administratorInvitationRepository
                .findByEmail(email);
        if (invitationOptional.isPresent()) {
            AdministratorInvitation invitation = invitationOptional.get();
            if (hasAcceptedInvitation(invitation)) {
                throw new BadRequestException(ErrorConstants.INVITATION_ACCEPTED);
            }
            throw new BadRequestException(ErrorConstants.INVITATION_SENT);
        }

        String invitationCode = generateInvitationCode();
        AdministratorInvitation invitation = new AdministratorInvitation();
        invitation.setEmail(email);
        invitation.setRole(role);
        invitation.setInvitationCode(invitationCode);

        AdministratorInvitation savedInvitation = administratorInvitationRepository.save(invitation);

        sendInvitationEmail(email, invitationCode);

        return savedInvitation;
    }

    @Override
    public void acceptInvitation(String invitationCode, UserAuthProfileRequest userProfileRequest) {
        AdministratorInvitation invitation = administratorInvitationRepository.findByInvitationCode(invitationCode)
                .orElseThrow(() -> new NotFoundException("Invitation not found"));

        if (invitation.getInvitationStatus() == Status.ACCEPTED) {
            throw new BadRequestException(ErrorConstants.INVITATION_ACCEPTED);
        }

        UserAuthProfile userAuthProfile = userAuthServiceImpl.saveNewUserAuthProfile(userProfileRequest);

        Administrator administrator = createAdministrator(userAuthProfile);
        administratorRepository.save(administrator);

        invitation.setInvitationStatus(Status.ACCEPTED);
        invitation.setAcceptedAt(LocalDateTime.now());
        administratorInvitationRepository.save(invitation);
    }

    private Administrator createAdministrator(UserAuthProfile userAuthProfile) {
        return Administrator.builder()
                .userAuthProfile(userAuthProfile)
                .build();
    }

    private boolean hasAcceptedInvitation(AdministratorInvitation item) {
        return Status.ACCEPTED.equals(item.getInvitationStatus());
    }

    private String generateInvitationCode() {
        return UUID.randomUUID().toString();
    }

    private void sendInvitationEmail(String email, String invitationCode) {
        String subject = "Invitation to join as administrator";
        String body = "You have been invited to join as an administrator. Use the following invitation code to register: " + invitationCode;

//        emailService.sendEmail(email, subject, body);
    }
}
