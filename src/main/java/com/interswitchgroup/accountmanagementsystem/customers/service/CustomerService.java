package com.interswitchgroup.accountmanagementsystem.customers.service;

import com.interswitchgroup.accountmanagementsystem.common.utils.PaginatedResponseDTO;
import com.interswitchgroup.accountmanagementsystem.customers.dto.CustomerCreationDTO;
import com.interswitchgroup.accountmanagementsystem.customers.dto.CustomerDTO;
import com.interswitchgroup.accountmanagementsystem.dashboard.dto.CustomerDetailsDto;

/**
 * @author Joy Osayi
 * @createdOn Apr-14(Sun)-2024
 */
public interface CustomerService {
    CustomerDTO createCustomer(CustomerCreationDTO request);

    PaginatedResponseDTO<CustomerDTO> getAllCustomers(int pageNumber, int pageSize);

    CustomerDetailsDto getCustomerById(Long customerId);

    CustomerDTO viewAuthenticatedCustomerDetails();
}
