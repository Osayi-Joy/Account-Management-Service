package com.interswitchgroup.accountmanagementsystem.authentication.service.impl;

import com.interswitchgroup.accountmanagementsystem.authentication.dto.UserAuthProfileDTO;
import com.interswitchgroup.accountmanagementsystem.authentication.dto.request.LoginRequestDTO;
import com.interswitchgroup.accountmanagementsystem.authentication.dto.response.LoginResponse;
import com.interswitchgroup.accountmanagementsystem.authentication.model.Permission;
import com.interswitchgroup.accountmanagementsystem.authentication.model.Role;
import com.interswitchgroup.accountmanagementsystem.authentication.model.UserAuthProfile;
import com.interswitchgroup.accountmanagementsystem.authentication.repository.UserAuthProfileRepository;
import com.interswitchgroup.accountmanagementsystem.authentication.service.AuthenticationService;
import com.interswitchgroup.accountmanagementsystem.authentication.service.LoginAttemptService;
import com.interswitchgroup.accountmanagementsystem.authentication.service.RoleService;
import com.interswitchgroup.accountmanagementsystem.common.config.security.JwtHelper;
import com.interswitchgroup.accountmanagementsystem.common.constants.ErrorConstants;
import com.interswitchgroup.accountmanagementsystem.common.enums.Status;
import com.interswitchgroup.accountmanagementsystem.common.exception.BadRequestException;
import com.interswitchgroup.accountmanagementsystem.common.exception.NotFoundException;
import com.interswitchgroup.accountmanagementsystem.common.utils.CommonUtils;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Joy Osayi
 * @createdOn Apr-13(Sat)-2024
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements UserDetailsService, AuthenticationService {
    private final RoleService roleServiceImpl;
    private final UserAuthProfileRepository userAuthProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;
    LoginAttemptService loginAttemptServiceImpl;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAuthProfile userFoundInDB =
                userAuthProfileRepository
                        .findFirstByUsernameOrderByCreatedDate(username)
                        .orElseThrow(
                                () -> new NotFoundException(ErrorConstants.LOGIN_FAILED));
        if (Status.INACTIVE.equals(userFoundInDB.getStatus()))
            throw new BadRequestException(ErrorConstants.PROFILE_DISABLED);

        return getUserProfileDTO(username, userFoundInDB);
    }

    @Override
    public LoginResponse authenticate(LoginRequestDTO loginRequestDTO) {
        UserAuthProfileDTO userDetails =
                (UserAuthProfileDTO) this.loadUserByUsername(loginRequestDTO.getUsername());
        roleServiceImpl.checkRoleStatus(userDetails.getAssignedRole());
        if (passwordEncoder.matches(loginRequestDTO.getPassword(), userDetails.getPassword())) {
            loginAttemptServiceImpl.verifyLoginAccess(userDetails.getUsername(), true);
            return getLoginResponse(loginRequestDTO, userDetails);
        }

        loginAttemptServiceImpl.verifyLoginAccess(userDetails.getUsername(), false);
    throw new BadRequestException(ErrorConstants.LOGIN_FAILED);
    }



    public LoginResponse getLoginResponse(
            LoginRequestDTO loginRequestDTO, UserAuthProfileDTO userDetails) {
        Map<String, String> claims =
                CommonUtils.getClaims(loginRequestDTO.getUsername(), userDetails);
        Map<String, Object> additionalInformation = new HashMap<>();
        additionalInformation.put("firstName", userDetails.getFirstName());
        additionalInformation.put("email", userDetails.getEmail());
        additionalInformation.put("role", userDetails.getAssignedRole());
        additionalInformation.put(
                "name", userDetails.getFirstName().concat(" ").concat(userDetails.getLastName()));
        return LoginResponse.builder()
                .accessToken(jwtHelper.createJwtForClaims(loginRequestDTO.getUsername(), claims))
                .additionalInformation(additionalInformation)
                .build();
    }
    

    private Set<SimpleGrantedAuthority> getGrantedAuthorities(String assignedRole) {
        Role backOfficeRole = roleServiceImpl.retrieveRole(assignedRole);
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        for (Permission permission : backOfficeRole.getPermissions()) {
            authorities.add(new SimpleGrantedAuthority(permission.getName()));
        }
        return authorities;
    }
    



    private UserAuthProfileDTO getUserProfileDTO(
            String username, UserAuthProfile userFoundInDB) {
        UserAuthProfileDTO userProfileDTO = new UserAuthProfileDTO();
        userProfileDTO.setUsername(username);
        userProfileDTO.setFirstName(userFoundInDB.getFirstName());
        userProfileDTO.setLastName(userFoundInDB.getLastName());
        userProfileDTO.setAssignedRole(userFoundInDB.getAssignedRole());
        userProfileDTO.setEmail(userFoundInDB.getEmail());
        userProfileDTO.setPassword(userFoundInDB.getPassword());
        userProfileDTO.setPermissions(getGrantedAuthorities(userFoundInDB.getAssignedRole()));

        return userProfileDTO;
    }
}

