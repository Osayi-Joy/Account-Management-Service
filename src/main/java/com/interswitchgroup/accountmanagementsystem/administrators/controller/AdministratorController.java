package com.interswitchgroup.accountmanagementsystem.administrators.controller;
/**
 * @author Joy Osayi
 * @createdOn Apr-12(Fri)-2024
 */
import com.interswitchgroup.accountmanagementsystem.administrators.service.AdministratorService;
import com.interswitchgroup.accountmanagementsystem.authentication.dto.request.UserAuthProfileRequest;
import com.interswitchgroup.accountmanagementsystem.common.utils.SwaggerDocUtil;
import com.interswitchgroup.accountmanagementsystem.common.utils.response.ControllerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.interswitchgroup.accountmanagementsystem.common.constants.Constants.ADMINISTRATOR_API_V1;

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
        return ControllerResponse.buildSuccessResponse( administratorService.inviteAdministrator(email, role));
    }

    @PostMapping("/accept-invitation/{invitationCode}")
    @Operation(
            summary = SwaggerDocUtil.ADMIN_CONTROLLER_ACCEPT_INVITATION_TITLE,
            description = SwaggerDocUtil.ADMIN_CONTROLLER_ACCEPT_INVITATION_DESCRIPTION
    )
    public ResponseEntity<Object> acceptInvitation(@PathVariable("invitationCode") String invitationCode,
                                                   @RequestBody UserAuthProfileRequest userProfileRequest) {
        administratorService.acceptInvitation(invitationCode, userProfileRequest);
        return ControllerResponse.buildSuccessResponse();
    }
}
