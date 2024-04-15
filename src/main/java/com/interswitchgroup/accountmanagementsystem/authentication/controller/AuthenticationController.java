package com.interswitchgroup.accountmanagementsystem.authentication.controller;
/**
 * @author Joy Osayi
 * @createdOn Apr-14(Sun)-2024
 */
import com.interswitchgroup.accountmanagementsystem.authentication.dto.request.LoginRequestDTO;
import com.interswitchgroup.accountmanagementsystem.authentication.dto.response.LoginResponse;
import com.interswitchgroup.accountmanagementsystem.authentication.service.AuthenticationService;
import com.interswitchgroup.accountmanagementsystem.common.utils.SwaggerDocUtil;
import com.interswitchgroup.accountmanagementsystem.common.utils.response.ControllerResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import static com.interswitchgroup.accountmanagementsystem.common.constants.Constants.AUTHENTICATION_API_VI;

@RestController
@RequestMapping(AUTHENTICATION_API_VI)
@Tag(name = SwaggerDocUtil.AUTHENTICATION_CONTROLLER_TITLE, description = SwaggerDocUtil.AUTHENTICATION_CONTROLLER_DESCRIPTION)
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(
            summary = SwaggerDocUtil.AUTHENTICATION_CONTROLLER_LOGIN_TITLE,
            description = SwaggerDocUtil.AUTHENTICATION_CONTROLLER_LOGIN_DESCRIPTION
    )
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponse response = authenticationService.authenticate(loginRequestDTO);
        return ControllerResponse.buildSuccessResponse(response);
    }
}
