package com.interswitchgroup.accountmanagementsystem.common.background.implementation;


import com.fasterxml.jackson.core.type.TypeReference;
import com.interswitchgroup.accountmanagementsystem.administrators.service.AdministratorService;
import com.interswitchgroup.accountmanagementsystem.authentication.dto.PermissionDTO;
import com.interswitchgroup.accountmanagementsystem.authentication.dto.request.RoleCreationDTO;
import com.interswitchgroup.accountmanagementsystem.authentication.model.Permission;
import com.interswitchgroup.accountmanagementsystem.authentication.model.Role;
import com.interswitchgroup.accountmanagementsystem.authentication.service.PermissionService;
import com.interswitchgroup.accountmanagementsystem.authentication.service.RoleService;
import com.interswitchgroup.accountmanagementsystem.authentication.service.UserAuthService;
import com.interswitchgroup.accountmanagementsystem.common.utils.CommonUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import static com.interswitchgroup.accountmanagementsystem.common.constants.Constants.*;
@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BackgroundStartUpTaskImpl {
  private final RoleService roleServiceImpl;
  private final PermissionService permissionServiceImpl;
  @Value("${account-service.systemDefinedPermissions}")
  private String systemDefinedPermissions;
  private final AdministratorService administratorService;
  private final UserAuthService userAuthProfileServiceImpl;


 
  @EventListener(ContextRefreshedEvent.class)
  public void runSystemStartUpTask() {
    updateSystemPermissions();
    createOrUpdateSuperAdminRole();
    createOrUpdateAdminRole();
    createUserRole();
    administratorService.saveSuperAdmin();
  }


  private void updateSystemPermissions() {
    File file = getSystemFile(systemDefinedPermissions);
    Set<Permission> newAuthorities;
    try {
      newAuthorities = CommonUtils.getObjectMapper().readValue(file, new TypeReference<>() {});
    } catch (IOException ignored) {
      log.trace("no update required");
      return;
    }
    if ("systemPermissionUpdate.json".equals(file.getName())) {
      permissionServiceImpl.addOrUpdateSystemPermissions(newAuthorities);
    }
  }

  private void createOrUpdateSuperAdminRole() {
    boolean roleExists = roleServiceImpl.checkIfRoleExists(ROLE_SUPER_ADMIN);
    if (roleExists) {
      Role existingRole = roleServiceImpl.retrieveRole(ROLE_SUPER_ADMIN);
      Set<String> existingPermissions = existingRole.getPermissions().stream()
              .map(Permission::getName)
              .collect(Collectors.toSet());

      Set<String> allSystemPermissions = permissionServiceImpl.retrieveAllSystemPermissions().stream()
              .map(PermissionDTO::getName)
              .collect(Collectors.toSet());

      Set<String> newPermissions = allSystemPermissions.stream()
              .filter(permission -> !existingPermissions.contains(permission))
              .collect(Collectors.toSet());

      if (!newPermissions.isEmpty()) {
        existingRole.getPermissions().addAll(
                permissionServiceImpl.retrieveValidPermissions(newPermissions));
        roleServiceImpl.updateExistingRole(RoleCreationDTO.builder()
                        .name(existingRole.getName())
                        .description(existingRole.getDescription())
                        .permissions(existingRole.getPermissions()
                                .stream().map(Permission::getName).collect(Collectors.toSet()))
                .build());
        //TODO USERS PERMISSIONS
        userAuthProfileServiceImpl.updateUsersPermissions(existingRole);
      }
    } else {
      RoleCreationDTO superAdminDTO = RoleCreationDTO.builder()
              .name(ROLE_SUPER_ADMIN)
              .description("Super Admin Role")
              .permissions(permissionServiceImpl.retrieveAllSystemPermissions().stream()
                      .map(PermissionDTO::getName)
                      .collect(Collectors.toSet()))
              .build();
      roleServiceImpl.createCustomNewRole(superAdminDTO);
    }
  }

  private void createOrUpdateAdminRole() {
    createOrUpdateRole(ROLE_ADMIN, "ADMIN", "Admin Role");
  }


  private void createUserRole() {
    createOrUpdateRole(ROLE_USER, "USERS", "User Role");
  }

  private void createOrUpdateRole(String roleName, String roleType, String roleDescription) {
    boolean roleExists = roleServiceImpl.checkIfRoleExists(roleName);
    if (roleExists) {
      Role existingRole = roleServiceImpl.retrieveRole(roleName);
      Set<String> existingPermissions = existingRole.getPermissions().stream()
              .map(Permission::getName)
              .collect(Collectors.toSet());

      Set<String> rolePermissions = permissionServiceImpl.retrievePermissionsByType(roleType).stream()
              .map(Permission::getName)
              .collect(Collectors.toSet());

      Set<String> newPermissions = rolePermissions.stream()
              .filter(permission -> !existingPermissions.contains(permission))
              .collect(Collectors.toSet());

      if (!newPermissions.isEmpty()) {
        existingRole.getPermissions().addAll(permissionServiceImpl.retrieveValidPermissions(newPermissions));
        roleServiceImpl.updateExistingRole(RoleCreationDTO.builder()
                .name(existingRole.getName())
                .description(existingRole.getDescription())
                .permissions(existingRole.getPermissions().stream().map(Permission::getName).collect(Collectors.toSet()))
                .build());
        userAuthProfileServiceImpl.updateUsersPermissions(existingRole);
      }
    } else {
      RoleCreationDTO roleDTO = RoleCreationDTO.builder()
              .name(roleName)
              .description(roleDescription)
              .permissions(permissionServiceImpl.retrievePermissionsByType(roleType).stream()
                      .map(Permission::getName)
                      .collect(Collectors.toSet()))
              .build();
      roleServiceImpl.createCustomNewRole(roleDTO);
    }
  }

  private File getSystemFile(String filePath) {
    Path path = Paths.get(filePath);
    return path.toFile();
  }

}
