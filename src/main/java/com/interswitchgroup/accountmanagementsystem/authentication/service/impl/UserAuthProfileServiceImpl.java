package com.interswitchgroup.accountmanagementsystem.authentication.service.impl;

import com.interswitchgroup.accountmanagementsystem.authentication.dto.request.UserAuthProfileRequest;
import com.interswitchgroup.accountmanagementsystem.authentication.model.Permission;
import com.interswitchgroup.accountmanagementsystem.authentication.model.Role;
import com.interswitchgroup.accountmanagementsystem.authentication.model.UserAuthProfile;
import com.interswitchgroup.accountmanagementsystem.authentication.repository.UserAuthProfileRepository;
import com.interswitchgroup.accountmanagementsystem.authentication.service.PermissionService;
import com.interswitchgroup.accountmanagementsystem.authentication.service.RoleService;
import com.interswitchgroup.accountmanagementsystem.authentication.service.UserAuthService;
import com.interswitchgroup.accountmanagementsystem.common.constants.ErrorConstants;
import com.interswitchgroup.accountmanagementsystem.common.exception.BadRequestException;
import com.interswitchgroup.accountmanagementsystem.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.interswitchgroup.accountmanagementsystem.common.utils.CommonUtils.*;

/**
 * @author Joy Osayi
 * @createdOn Apr-12(Fri)-2024
 */
@Service
@RequiredArgsConstructor
public class UserAuthProfileServiceImpl implements UserAuthService {
    private final RoleService roleServiceImpl;
    private final UserAuthProfileRepository userAuthProfileRepository;
    private final PasswordEncoder passwordEncoder;



    //TODO Method to saveNewUserAuthProfile to achieve this
    // some checks to make the,
    // check the username and email doesn't already exist,
    // call the static methods to validate the email, phonenumber, and password
    // check that the role exists
    // fetch the permissions from the role and pass it into the permissions of the user auth profile
    //


    @Override
    public UserAuthProfile saveNewUserAuthProfile(UserAuthProfileRequest request) {
        validateUserAuthProfileRequest(request);
        Role roleDTO = roleServiceImpl.retrieveRole(request.getAssignedRole());
        if (roleDTO == null) {
            throw new NotFoundException(ErrorConstants.ROLE_NOT_FOUND);
        }
        Set<Permission> permissions = roleDTO.getPermissions();
        UserAuthProfile userAuthProfile = new UserAuthProfile();
        userAuthProfile.setUsername(request.getUsername());
        userAuthProfile.setPassword(passwordEncoder.encode(request.getPassword()));
        userAuthProfile.setEmail(request.getEmail());
        userAuthProfile.setFirstName(request.getFirstName());
        userAuthProfile.setLastName(request.getLastName());
        userAuthProfile.setPhoneNumber(request.getPhoneNumber());
        userAuthProfile.setAssignedRole(request.getAssignedRole());
        userAuthProfile.setPermissions(permissions);

        return userAuthProfileRepository.save(userAuthProfile);
    }

    private void validateUserAuthProfileRequest(UserAuthProfileRequest request) {
        if (userAuthProfileRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException(ErrorConstants.USERNAME_ALREADY_EXISTS);
        }
        if (userAuthProfileRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException(ErrorConstants.EMAIL_ALREADY_EXISTS);
        }
        if (!validateEmail(request.getEmail())) {
            throw new BadRequestException(ErrorConstants.INVALID_EMAIL_FORMAT);
        }
        if (request.getPhoneNumber() != null && !validatePhoneNumber(request.getPhoneNumber())) {
            throw new BadRequestException(ErrorConstants.INVALID_PHONE_NUMBER_FORMAT);
        }
        if (!validatePassword(request.getPassword())) {
            throw new BadRequestException(ErrorConstants.INVALID_PASSWORD_FORMAT);
        }

    }

}
