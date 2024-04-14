package com.interswitchgroup.accountmanagementsystem.authentication.service.impl;

import com.interswitchgroup.accountmanagementsystem.authentication.dto.PermissionDTO;
import com.interswitchgroup.accountmanagementsystem.authentication.model.Permission;
import com.interswitchgroup.accountmanagementsystem.authentication.repository.PermissionRepository;
import com.interswitchgroup.accountmanagementsystem.authentication.service.PermissionService;
import com.interswitchgroup.accountmanagementsystem.common.constants.ErrorConstants;
import com.interswitchgroup.accountmanagementsystem.common.exception.NotFoundException;
import com.interswitchgroup.accountmanagementsystem.common.utils.BeanUtilWrapper;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
  private final PermissionRepository permissionRepository;

  @Override
  public Set<PermissionDTO> retrieveAllSystemPermissions() {
    return permissionRepository.findAll().stream()
            .map(this::mapEntityToDTO)
            .collect(Collectors.toSet());
  }
  @Override
  public Set<Permission> retrieveSystemPermissions() {
    return new HashSet<>(permissionRepository.findAll());
  }

  @Override
  public PermissionDTO retrieveSystemPermissionDto(String name) {
    Permission permission = permissionRepository.findFirstByNameOrderByCreatedDate(name)
            .orElseThrow(() -> new NotFoundException(ErrorConstants.PERMISSION_NOT_FOUND));
    return mapEntityToDTO(permission);
  }

  @Override
  public Permission retrieveSystemPermissions(String name) {
    return permissionRepository.findFirstByNameOrderByCreatedDate(name)
            .orElseThrow(() -> new NotFoundException(ErrorConstants.PERMISSION_NOT_FOUND));
  }

  @Override
  @Transactional
  public void addOrUpdateSystemPermissions(Set<Permission> newPermissions) {
    newPermissions.forEach(
            permission -> {
              Optional<Permission> existingPermission =
                      permissionRepository.findFirstByNameOrderByCreatedDate(permission.getName());
              if (existingPermission.isPresent()) {
                permission.setId(existingPermission.get().getId());
                permission.setPermissionType(existingPermission.get().getPermissionType());
                permission.setName(existingPermission.get().getName());
                permission.setDeleted(existingPermission.get().isDeleted());
                permission.setDescription(existingPermission.get().getDescription());
                permission.setPermissionType(existingPermission.get().getPermissionType());
              }
            });
    permissionRepository.saveAll(newPermissions);
  }
//  @Override
//  public void addSystemPermissions(Set<Permission> newPermissions) {
//    newPermissions.forEach(
//            permission -> {
//              Optional<Permission> existingPermission =
//                      permissionRepository.findFirstByNameOrderByCreatedDate(permission.getName());
//              if (existingPermission.isPresent()) {
//                permission.setId(existingPermission.get().getId());
//                permission.setPermissionType(existingPermission.get().getPermissionType());
//                permission.setName(existingPermission.get().getName());
//                permission.setDeleted(existingPermission.get().isDeleted());
//                permission.setDescription(existingPermission.get().getDescription());
//                permission.setPermissionType(existingPermission.get().getPermissionType());
//              }
//            });
//    permissionRepository.saveAll(newPermissions);
//  }
  @Override
  public Set<Permission> retrieveValidPermissions(Set<String> permissionNames) {
    return permissionRepository.findAllByNameIn(permissionNames);
  }

  @Override
  public Set<Permission> retrievePermissionsByType(String permissionType) {
    return permissionRepository.findByPermissionType(permissionType);
  }

  public PermissionDTO mapEntityToDTO(Permission permission) {
    PermissionDTO permissionDTO = new PermissionDTO();
    BeanUtilWrapper.copyNonNullProperties(permission, permissionDTO);
    return permissionDTO;
  }
}
