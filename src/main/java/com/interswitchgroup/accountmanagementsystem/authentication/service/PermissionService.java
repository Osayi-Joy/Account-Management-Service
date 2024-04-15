package com.interswitchgroup.accountmanagementsystem.authentication.service;


import com.interswitchgroup.accountmanagementsystem.authentication.dto.PermissionDTO;
import com.interswitchgroup.accountmanagementsystem.authentication.model.Permission;

import java.util.Set;

public interface PermissionService {

  Set<PermissionDTO> retrieveAllSystemPermissions();

  Set<Permission> retrieveSystemPermissions();

  PermissionDTO retrieveSystemPermissionDto(String name);

  Permission retrieveSystemPermissions(String name);

  void addOrUpdateSystemPermissions(Set<Permission> newPermissions);

  Set<Permission> retrieveValidPermissions(Set<String> permissionNames);

  Set<Permission> retrievePermissionsByType(String permissionType);

  PermissionDTO mapEntityToDTO(Permission permission);

}
