package com.interswitchgroup.accountmanagementsystem.administrators.controller;
/**
 * @author Joy Osayi
 * @createdOn Apr-12(Fri)-2024
 */
import static com.interswitchgroup.accountmanagementsystem.common.constants.Constants.*;

import com.interswitchgroup.accountmanagementsystem.administrators.dto.AcceptInvitationRequest;
import com.interswitchgroup.accountmanagementsystem.administrators.service.AdministratorService;
import com.interswitchgroup.accountmanagementsystem.common.utils.SwaggerDocUtil;
import com.interswitchgroup.accountmanagementsystem.common.utils.response.ControllerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ADMINISTRATOR_API_V1)
@Tag(name = SwaggerDocUtil.ADMIN_CONTROLLER_TITLE, description = SwaggerDocUtil.ADMIN_CONTROLLER_DESCRIPTION)
@RequiredArgsConstructor
public class AdministratorController {

    private final AdministratorService administratorService;

    @PostMapping("/invite")
    @PreAuthorize("hasAuthority('invite-administrative-user')")
    @Operation(
            summary = SwaggerDocUtil.ADMIN_CONTROLLER_INVITE_ADMIN_TITLE,
            description = SwaggerDocUtil.ADMIN_CONTROLLER_INVITE_ADMIN_DESCRIPTION
    )
    public ResponseEntity<Object> inviteAdministrator(@RequestParam("email") String email, @RequestParam("role") String role) {
        administratorService.inviteAdministrator(email, role);
        return ControllerResponse.buildSuccessResponse("Invitation sent successfully");
    }

    @PostMapping("/accept-invitation/{invitationCode}")
    @Operation(
            summary = SwaggerDocUtil.ADMIN_CONTROLLER_ACCEPT_INVITATION_TITLE,
            description = SwaggerDocUtil.ADMIN_CONTROLLER_ACCEPT_INVITATION_DESCRIPTION
    )
    public ResponseEntity<Object> acceptInvitation(@PathVariable("invitationCode") String invitationCode,
                                                   @RequestBody AcceptInvitationRequest acceptInvitationRequest) {
        return ControllerResponse.buildSuccessResponse(
                administratorService.acceptInvitation(invitationCode, acceptInvitationRequest));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('view-administrative-users')")
    @Operation(
            summary = SwaggerDocUtil.GET_ALL_ADMINISTRATORS_SUMMARY,
            description = SwaggerDocUtil.GET_ALL_ADMINISTRATORS_DESCRIPTION
    )
    public ResponseEntity<Object> getAllAdministrators(
            @RequestParam(value = PAGE_NUMBER, defaultValue = PAGE_NUMBER_DEFAULT_VALUE, required = false) int pageNumber,
            @RequestParam(value = PAGE_SIZE, defaultValue = PAGE_SIZE_DEFAULT_VALUE, required = false) int pageSize) {
        return ControllerResponse.buildSuccessResponse(
                administratorService.getAllAdministrators(pageNumber, pageSize));
    }

    @GetMapping("/{administratorId}")
    @PreAuthorize("hasAuthority('view-administrative-user-details')")
    @Operation(
            summary = SwaggerDocUtil.GET_ADMINISTRATOR_BY_ID_SUMMARY,
            description = SwaggerDocUtil.GET_ADMINISTRATOR_BY_ID_DESCRIPTION
    )
    public ResponseEntity<Object> getAdministratorById(@PathVariable Long administratorId) {
        return ControllerResponse.buildSuccessResponse(
                administratorService.getAdministratorById(administratorId));
    }
    @GetMapping("/details")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = SwaggerDocUtil.VIEW_AUTHENTICATED_ADMINISTRATOR_SUMMARY,
            description = SwaggerDocUtil.VIEW_AUTHENTICATED_ADMINISTRATOR_DESCRIPTION)
    public ResponseEntity<Object> viewAuthenticatedAdministratorDetails() {
        return ControllerResponse.buildSuccessResponse(
                administratorService.viewAuthenticatedAdministratorDetails());
    }
}
