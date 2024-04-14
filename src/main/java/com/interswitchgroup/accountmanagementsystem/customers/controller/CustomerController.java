package com.interswitchgroup.accountmanagementsystem.customers.controller;

import com.interswitchgroup.accountmanagementsystem.common.utils.SwaggerDocUtil;
import com.interswitchgroup.accountmanagementsystem.common.utils.response.ControllerResponse;
import com.interswitchgroup.accountmanagementsystem.customers.dto.CustomerCreationDTO;
import com.interswitchgroup.accountmanagementsystem.customers.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.interswitchgroup.accountmanagementsystem.common.constants.Constants.*;
import static com.interswitchgroup.accountmanagementsystem.common.utils.SwaggerDocUtil.VIEW_AUTHENTICATED_CUSTOMER_DESCRIPTION;
import static com.interswitchgroup.accountmanagementsystem.common.utils.SwaggerDocUtil.VIEW_AUTHENTICATED_CUSTOMER_SUMMARY;

/**
 * @author Joy Osayi
 * @createdOn Apr-14(Sun)-2024
 */
@RestController
@RequestMapping(CUSTOMER_API_V1)
@Tag(name = SwaggerDocUtil.CUSTOMER_CONTROLLER_TITLE, description = SwaggerDocUtil.CUSTOMER_CONTROLLER_DESCRIPTION)
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/create")
    @Operation(
            summary = SwaggerDocUtil.CREATE_CUSTOMER_SUMMARY,
            description = SwaggerDocUtil.CREATE_CUSTOMER_DESCRIPTION
    )
    public ResponseEntity<Object> createCustomer(@RequestBody CustomerCreationDTO request) {
        return ControllerResponse.buildSuccessResponse(customerService.createCustomer(request));
    }

  @GetMapping
  @PreAuthorize("hasAuthority('view-users')")
  @Operation(
      summary = SwaggerDocUtil.GET_ALL_CUSTOMERS_SUMMARY,
      description = SwaggerDocUtil.GET_ALL_CUSTOMERS_DESCRIPTION)
  public ResponseEntity<Object> getAllCustomers(
      @RequestParam(value = PAGE_NUMBER, defaultValue = PAGE_NUMBER_DEFAULT_VALUE, required = false)
          int pageNumber,
      @RequestParam(value = PAGE_SIZE, defaultValue = PAGE_SIZE_DEFAULT_VALUE, required = false)
          int pageSize) {
        return ControllerResponse.buildSuccessResponse(
                customerService.getAllCustomers(pageNumber, pageSize));
    }

    @GetMapping("/{customerId}")
    @PreAuthorize("hasAuthority('view-user-details')")
    @Operation(
            summary = SwaggerDocUtil.GET_CUSTOMER_BY_ID_SUMMARY,
            description = SwaggerDocUtil.GET_CUSTOMER_BY_ID_DESCRIPTION
    )
    public ResponseEntity<Object> getCustomerById(@PathVariable Long customerId) {
        return ControllerResponse.buildSuccessResponse(
                customerService.getCustomerById(customerId));
    }

    @GetMapping("/details")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = VIEW_AUTHENTICATED_CUSTOMER_SUMMARY,
            description = VIEW_AUTHENTICATED_CUSTOMER_DESCRIPTION)
    public ResponseEntity<Object> viewAuthenticatedCustomerDetails() {
        return ControllerResponse.buildSuccessResponse(
                customerService.viewAuthenticatedCustomerDetails());
    }
}
