package com.interswitchgroup.accountmanagementsystem.authentication.service.impl;

import com.interswitchgroup.accountmanagementsystem.authentication.dto.request.RoleCreationDTO;
import com.interswitchgroup.accountmanagementsystem.authentication.dto.RoleDTO;
import com.interswitchgroup.accountmanagementsystem.authentication.model.Role;
import com.interswitchgroup.accountmanagementsystem.authentication.repository.RoleRepository;
import com.interswitchgroup.accountmanagementsystem.authentication.service.PermissionService;
import com.interswitchgroup.accountmanagementsystem.authentication.service.RoleService;
import com.interswitchgroup.accountmanagementsystem.authentication.service.UserAuthService;
import com.interswitchgroup.accountmanagementsystem.common.constants.ErrorConstants;
import com.interswitchgroup.accountmanagementsystem.common.exception.BadRequestException;
import com.interswitchgroup.accountmanagementsystem.common.exception.NotFoundException;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.interswitchgroup.accountmanagementsystem.common.constants.Constants.CREATED_DATE;
import static com.interswitchgroup.accountmanagementsystem.common.constants.Constants.ROLE_ADMIN;


@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;
  private final PermissionService permissionServiceImpl;

  @Override
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
  public RoleDTO createCustomNewRole(RoleCreationDTO request) {
    if (checkIfRoleExists(request.getName())) {
      throw new BadRequestException(ErrorConstants.ROLE_ALREADY_EXISTS);
    }
    permissionCheck(request);
    Role newRole = mapRoleDTOToEntity(request);
    newRole.setPermissions(
            permissionServiceImpl.retrieveValidPermissions(request.getPermissions()));
    return mapRoleEntityToDTO(roleRepository.save(newRole));
  }

  @Override
  public Role retrieveRole(String name) {
    return roleRepository
            .findFirstByNameAndIsDeletedFalseOrderByCreatedDate(name)
            .orElseThrow(
                    () ->
                            new NotFoundException(ErrorConstants.ROLE_NOT_FOUND));
  }

  @Override
  public RoleDTO retrieveSystemRole(String name) {
    Role backOfficeRole = retrieveRole(name);
    RoleDTO dtoWithTeamMembers = new RoleDTO();
    dtoWithTeamMembers.setName(backOfficeRole.getName());
    dtoWithTeamMembers.setDescription(backOfficeRole.getDescription());
    dtoWithTeamMembers.setPermissions(
            backOfficeRole.getPermissions().stream()
                    .map(permissionServiceImpl::mapEntityToDTO)
                    .collect(Collectors.toSet()));
    return dtoWithTeamMembers;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
  public Page<RoleDTO> retrieveAllRoles(
          int pageNumber, int pageSize) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(CREATED_DATE).descending());
    Page<Role> rolePage = roleRepository.findAllByIsDeletedFalse(pageable);

    return rolePage.map(this::mapRoleEntityToDTO);
  }

  @Override
  public void updateExistingRole(RoleCreationDTO roleDTO) {
    Role role = retrieveRole(roleDTO.getName());
    role.setDescription(roleDTO.getDescription());
    role.setPermissions(permissionServiceImpl.retrieveValidPermissions(roleDTO.getPermissions()));
    roleRepository.save(role);
  }

  @Override
  public void validateAdministratorRole(String roleName) {
    Role role = retrieveRole(roleName);
    if (!role.getName().equalsIgnoreCase(ROLE_ADMIN)) {
      throw new IllegalArgumentException(String.format(ErrorConstants.INVALID_ADMINISTRATOR_ROLE, roleName));
    }
  }

  @Override
  public void checkRoleStatus(String name) {
    retrieveRole(name);
  }
  @Override
  public boolean checkIfRoleExists(String name) {
    return roleRepository.existsByName(name);
  }

  @Override
  public void permissionCheck(RoleCreationDTO roleCreationDTO) {
    if (roleCreationDTO.getPermissions() == null || roleCreationDTO.getPermissions().isEmpty()) {
      throw new BadRequestException(ErrorConstants.PERMISSIONS_REQUIRED);
    }
  }

  @Override
  public void roleCheck(String name) {
    if (!checkIfRoleExists(name))
      throw new BadRequestException(ErrorConstants.ROLE_DOES_NOT_EXIST);
  }

  public RoleDTO mapRoleEntityToDTO(Role role) {
    RoleDTO roleDTO = new RoleDTO();
    BeanUtils.copyProperties(role, roleDTO);
    roleDTO.setPermissions(
            role.getPermissions().stream()
                    .map(permissionServiceImpl::mapEntityToDTO)
                    .collect(Collectors.toSet()));
    return roleDTO;
  }

  public Role mapRoleDTOToEntity(RoleCreationDTO roleDTO) {
    Role role = new Role();
    BeanUtils.copyProperties(roleDTO, role);
    return role;
  }

}
