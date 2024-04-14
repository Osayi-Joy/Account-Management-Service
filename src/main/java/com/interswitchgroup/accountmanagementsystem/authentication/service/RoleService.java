package com.interswitchgroup.accountmanagementsystem.authentication.service;


import com.interswitchgroup.accountmanagementsystem.authentication.dto.request.RoleCreationDTO;
import com.interswitchgroup.accountmanagementsystem.authentication.dto.RoleDTO;
import com.interswitchgroup.accountmanagementsystem.authentication.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface RoleService {

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    Page<RoleDTO> retrieveAllRoles(
            int pageNumber, int pageSize);

    Role retrieveRole(String name);

    RoleDTO retrieveSystemRole(String name);

    void roleCheck(String name);

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    RoleDTO createCustomNewRole(RoleCreationDTO request);

    void permissionCheck(RoleCreationDTO roleCreationDTO);

    void updateExistingRole(RoleCreationDTO roleDTO);

    void validateAdministratorRole(String role);

    void checkRoleStatus(String assignedRole);
}
