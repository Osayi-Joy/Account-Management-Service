package com.interswitchgroup.accountmanagementsystem.authentication.controller;

import com.interswitchgroup.accountmanagementsystem.authentication.dto.PermissionDTO;
import com.interswitchgroup.accountmanagementsystem.authentication.dto.RoleDTO;
import com.interswitchgroup.accountmanagementsystem.authentication.dto.request.RoleCreationDTO;
import com.interswitchgroup.accountmanagementsystem.authentication.service.PermissionService;
import com.interswitchgroup.accountmanagementsystem.authentication.service.RoleService;
import com.interswitchgroup.accountmanagementsystem.common.utils.SwaggerDocUtil;
import com.interswitchgroup.accountmanagementsystem.common.utils.response.ControllerResponse;
import java.util.Set;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.interswitchgroup.accountmanagementsystem.common.constants.Constants.*;

/**
 * @author Joy Osayi
 * @createdOn Apr-14(Sun)-2024
 */
@RestController
@RequestMapping(ROLES_API_V1)
@RequiredArgsConstructor
@Tag(name = SwaggerDocUtil.ROLE_CONTROLLER_TITLE, description = SwaggerDocUtil.ROLE_CONTROLLER_DESCRIPTION)
public class RoleController {

    private final RoleService roleService;
    private final PermissionService permissionService;

  @GetMapping("/permissions")
  @PreAuthorize("hasAuthority('view-permissions')")
  @Operation(
      summary = SwaggerDocUtil.ROLE_CONTROLLER_GET_ALL_PERMISSIONS_TITLE,
      description = SwaggerDocUtil.ROLE_CONTROLLER_GET_ALL_PERMISSIONS_DESCRIPTION)
  public ResponseEntity<Object> retrieveAllSystemPermissions() {
        Set<PermissionDTO> permissions = permissionService.retrieveAllSystemPermissions();
        return ControllerResponse.buildSuccessResponse(permissions);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('view-roles')")
    @Operation(
            summary = SwaggerDocUtil.ROLE_CONTROLLER_GET_ALL_ROLES_TITLE,
            description = SwaggerDocUtil.ROLE_CONTROLLER_GET_ALL_ROLES_DESCRIPTION)
    public ResponseEntity<Object> retrieveAllRoles(
            @RequestParam(name = PAGE_NUMBER, defaultValue = PAGE_NUMBER_DEFAULT_VALUE) int pageNumber,
            @RequestParam(name = PAGE_SIZE, defaultValue = PAGE_SIZE_DEFAULT_VALUE) int pageSize) {
        Page<RoleDTO> roles = roleService.retrieveAllRoles(pageNumber, pageSize);
        return ControllerResponse.buildSuccessResponse(roles);
    }

    @GetMapping("/{name}")
    @PreAuthorize("hasAuthority('view-role-details')")
    @Operation(
            summary = SwaggerDocUtil.ROLE_CONTROLLER_GET_ROLE_BY_NAME_TITLE,
            description = SwaggerDocUtil.ROLE_CONTROLLER_GET_ROLE_BY_NAME_DESCRIPTION)
    public ResponseEntity<Object> retrieveSystemRole(@PathVariable String name) {
        RoleDTO role = roleService.retrieveSystemRole(name);
        return ControllerResponse.buildSuccessResponse(role);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('create-roles')")
    @Operation(
            summary = SwaggerDocUtil.ROLE_CONTROLLER_CREATE_ROLE_TITLE,
            description = SwaggerDocUtil.ROLE_CONTROLLER_CREATE_ROLE_DESCRIPTION)
    public ResponseEntity<Object> createCustomNewRole(@RequestBody RoleCreationDTO request) {
        RoleDTO createdRole = roleService.createCustomNewRole(request);
        return ControllerResponse.buildSuccessResponse(createdRole);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('edit-role')")
    @Operation(
            summary = SwaggerDocUtil.ROLE_CONTROLLER_UPDATE_ROLE_TITLE,
            description = SwaggerDocUtil.ROLE_CONTROLLER_UPDATE_ROLE_DESCRIPTION)
    public ResponseEntity<Object> updateExistingRole(@RequestBody RoleCreationDTO roleDTO) {
        roleService.updateExistingRole(roleDTO);
        return ControllerResponse.buildSuccessResponse();
    }
}
