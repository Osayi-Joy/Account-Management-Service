package com.interswitchgroup.accountmanagementsystem.administrators.service.impl;

import static com.interswitchgroup.accountmanagementsystem.common.constants.Constants.*;
import static com.interswitchgroup.accountmanagementsystem.common.utils.CommonUtils.createPaginatedResponse;

import com.interswitchgroup.accountmanagementsystem.administrators.dto.AcceptInvitationRequest;
import com.interswitchgroup.accountmanagementsystem.administrators.dto.AdministratorDTO;
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
import com.interswitchgroup.accountmanagementsystem.common.email.EmailService;
import com.interswitchgroup.accountmanagementsystem.common.exception.BadRequestException;
import com.interswitchgroup.accountmanagementsystem.common.exception.NotFoundException;
import com.interswitchgroup.accountmanagementsystem.common.utils.BeanUtilWrapper;
import com.interswitchgroup.accountmanagementsystem.common.utils.CommonUtils;
import com.interswitchgroup.accountmanagementsystem.common.utils.PaginatedResponseDTO;
import jakarta.persistence.EntityExistsException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.interswitchgroup.accountmanagementsystem.common.enums.Status;

/**
 * @author Joy Osayi
 * @createdOn Apr-12(Fri)-2024
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdministratorServiceImpl implements AdministratorService {
    private final UserAuthService userAuthServiceImpl;
    private final AdministratorRepository administratorRepository;
    private final AdministratorInvitationRepository administratorInvitationRepository;
    private final RoleService roleServiceImpl;
    private final EmailService emailService;
    @Override
    public void saveSuperAdmin() {
        if (administratorRepository.existsByUserAuthProfile_Email(SUPER_ADMIN_EMAIL)) {
            return;
        }
        UserAuthProfileRequest superAdminRequest = new UserAuthProfileRequest();
        superAdminRequest.setUsername(SUPER_ADMIN_EMAIL);
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
    public void inviteAdministrator(String email, String role) {
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
        invitation.setInvitationStatus(Status.PENDING);

        administratorInvitationRepository.save(invitation);
        log.info("<< Invitation code {} >>", invitationCode);
        sendInvitationEmail(email, invitationCode);

    }

    @Override
    public AdministratorDTO acceptInvitation(String invitationCode, AcceptInvitationRequest acceptInvitationRequest) {
        AdministratorInvitation invitation = administratorInvitationRepository.findByInvitationCode(invitationCode)
                .orElseThrow(() -> new NotFoundException("Invitation not found"));

        if (invitation.getInvitationStatus() == Status.ACCEPTED) {
            throw new BadRequestException(ErrorConstants.INVITATION_ACCEPTED);
        }

    UserAuthProfile userAuthProfile =
        userAuthServiceImpl.saveNewUserAuthProfile(UserAuthProfileRequest.builder()
                        .username(invitation.getEmail())
                        .password(acceptInvitationRequest.getPassword())
                        .assignedRole(invitation.getRole())
                        .email(invitation.getEmail())
                        .firstName(acceptInvitationRequest.getFirstName())
                        .lastName(acceptInvitationRequest.getLastName())
                        .phoneNumber(acceptInvitationRequest.getPhoneNumber())
                .build());

        Administrator administrator = createAdministrator(userAuthProfile);
        var savedAdministrator = administratorRepository.save(administrator);

        invitation.setInvitationStatus(Status.ACCEPTED);
        invitation.setAcceptedAt(LocalDateTime.now());
        administratorInvitationRepository.save(invitation);

        return convertToDTO(savedAdministrator);
    }

    @Override
    public PaginatedResponseDTO<AdministratorDTO> getAllAdministrators(int pageNumber, int pageSize) {
        Page<Administrator> administratorPage = administratorRepository.findAll(
                PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, CREATED_DATE))
        );
        List<AdministratorDTO> administratorDTOList = administratorPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return createPaginatedResponse(administratorPage, administratorDTOList);
    }

    @Override
    public AdministratorDTO getAdministratorById(Long administratorId) {
        Administrator administrator = administratorRepository.findById(administratorId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorConstants.ADMINISTRATOR_NOT_FOUND));
        return convertToDTO(administrator);
    }

    @Override
    public AdministratorDTO viewAuthenticatedAdministratorDetails() {
        String username = CommonUtils.getLoggedInUsername();
        Administrator administrator = administratorRepository.findByUserAuthProfile_Email(username)
                .orElseThrow(() -> new EntityNotFoundException("Administrator not found"));

        return convertToDTO(administrator);
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

        emailService.sendEmail(email, subject, body);
    }

    private AdministratorDTO convertToDTO(Administrator administrator) {
        AdministratorDTO dto = new AdministratorDTO();
        dto.setUsername(administrator.getUserAuthProfile().getUsername());
        dto.setEmail(administrator.getUserAuthProfile().getEmail());
        dto.setFirstName(administrator.getUserAuthProfile().getFirstName());
        dto.setLastName(administrator.getUserAuthProfile().getLastName());
        dto.setPhoneNumber(administrator.getUserAuthProfile().getPhoneNumber());
        dto.setAssignedRole(administrator.getUserAuthProfile().getAssignedRole());
        dto.setStatus(administrator.getUserAuthProfile().getStatus());
        return dto;
    }
}
