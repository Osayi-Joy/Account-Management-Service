package com.interswitchgroup.accountmanagementsystem.common.background.implementation;


import com.fasterxml.jackson.core.type.TypeReference;
import com.interswitchgroup.accountmanagementsystem.authentication.dto.PermissionDTO;
import com.interswitchgroup.accountmanagementsystem.authentication.dto.request.RoleCreationDTO;
import com.interswitchgroup.accountmanagementsystem.authentication.model.Permission;
import com.interswitchgroup.accountmanagementsystem.authentication.service.PermissionService;
import com.interswitchgroup.accountmanagementsystem.authentication.service.RoleService;
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


 
  @EventListener(ContextRefreshedEvent.class)
  public void runSystemStartUpTask() {
    updateSystemPermissions();
//    createSuperAdminRole();
//    createAdminRole();
//    createUserRole();

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

  private void createSuperAdminRole() {
    RoleCreationDTO superAdminDTO = new RoleCreationDTO();
    superAdminDTO.setName(ROLE_SUPER_ADMIN);
    superAdminDTO.setDescription("Super Admin Role");
    superAdminDTO.setPermissions(permissionServiceImpl.retrieveAllSystemPermissions().stream()
            .map(PermissionDTO::getName)
            .collect(Collectors.toSet()));
    roleServiceImpl.createCustomNewRole(superAdminDTO);
  }

  private void createAdminRole() {
    RoleCreationDTO adminDTO = new RoleCreationDTO();
    adminDTO.setName(ROLE_ADMIN);
    adminDTO.setDescription("Admin Role");
    adminDTO.setPermissions(permissionServiceImpl.retrievePermissionsByType("ADMIN").stream()
            .map(Permission::getName)
            .collect(Collectors.toSet()));
    roleServiceImpl.createCustomNewRole(adminDTO);
  }

  private void createUserRole() {
    RoleCreationDTO userDTO = new RoleCreationDTO();
    userDTO.setName(ROLE_USER);
    userDTO.setDescription("User Role");
    userDTO.setPermissions(permissionServiceImpl.retrievePermissionsByType("USERS").stream()
            .map(Permission::getName)
            .collect(Collectors.toSet()));
    roleServiceImpl.createCustomNewRole(userDTO);
  }

  private File getSystemFile(String filePath) {
    Path path = Paths.get(filePath);
    return path.toFile();
  }

}
