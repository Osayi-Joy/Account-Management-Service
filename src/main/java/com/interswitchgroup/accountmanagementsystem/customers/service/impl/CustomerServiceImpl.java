package com.interswitchgroup.accountmanagementsystem.customers.service.impl;

import com.interswitchgroup.accountmanagementsystem.accounts.dto.AccountDto;
import com.interswitchgroup.accountmanagementsystem.accounts.dto.AccountResponseDto;
import com.interswitchgroup.accountmanagementsystem.accounts.service.AccountService;
import com.interswitchgroup.accountmanagementsystem.authentication.dto.request.UserAuthProfileRequest;
import com.interswitchgroup.accountmanagementsystem.authentication.model.UserAuthProfile;
import com.interswitchgroup.accountmanagementsystem.authentication.service.UserAuthService;
import com.interswitchgroup.accountmanagementsystem.common.constants.ErrorConstants;
import com.interswitchgroup.accountmanagementsystem.common.utils.CommonUtils;
import com.interswitchgroup.accountmanagementsystem.common.utils.PaginatedResponseDTO;
import com.interswitchgroup.accountmanagementsystem.customers.dto.CustomerCreationDTO;
import com.interswitchgroup.accountmanagementsystem.customers.dto.CustomerDTO;
import com.interswitchgroup.accountmanagementsystem.customers.model.Customer;
import com.interswitchgroup.accountmanagementsystem.customers.repository.CustomerRepository;
import com.interswitchgroup.accountmanagementsystem.customers.service.CustomerService;
import com.interswitchgroup.accountmanagementsystem.dashboard.dto.CustomerDetailsDto;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author Joy Osayi
 * @createdOn Apr-14(Sun)-2024
 */

import java.util.List;
import java.util.stream.Collectors;

import static com.interswitchgroup.accountmanagementsystem.common.constants.Constants.CREATED_DATE;
import static com.interswitchgroup.accountmanagementsystem.common.constants.Constants.ROLE_USER;
import static com.interswitchgroup.accountmanagementsystem.common.utils.CommonUtils.createPaginatedResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final UserAuthService userAuthServiceImpl;
    private final CustomerRepository customerRepository;
    private final AccountService accountService;

    @Override
    public CustomerDTO createCustomer(CustomerCreationDTO request) {

        if (customerRepository.existsByUserAuthProfile_Email(request.getEmail())) {
            throw new EntityExistsException(ErrorConstants.USER_PROFILE_EXISTS);
        }
        UserAuthProfile userAuthProfile = userAuthServiceImpl.saveNewUserAuthProfile(
                UserAuthProfileRequest.builder()
                        .username(request.getUsername())
                        .password(request.getPassword())
                        .assignedRole(ROLE_USER)
                        .email(request.getEmail())
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .phoneNumber(request.getPhoneNumber())
                        .build()
        );

        Customer customer = new Customer();
        customer.setUserAuthProfile(userAuthProfile);
        customer.setAddress(request.getAddress());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setGender(request.getGender());

       var savedCustomer = customerRepository.save(customer);

        accountService.createAccount(AccountDto.builder()
                .accountType(request.getAccountType())
                .customerId(savedCustomer.getId())
                .build());

        return convertToDTO(savedCustomer);
    }

    @Override
    public PaginatedResponseDTO<CustomerDTO> getAllCustomers(int pageNumber, int pageSize) {
        Page<Customer> customerPage = customerRepository.findAll(
                PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, CREATED_DATE))
        );
        List<CustomerDTO> customerDTOList = customerPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return createPaginatedResponse(customerPage, customerDTOList);
    }

    @Override
    public CustomerDetailsDto getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorConstants.CUSTOMER_NOT_FOUND));
        List<AccountResponseDto> accounts = accountService.getAccountByCustomerId(customer.getId());
        return CustomerDetailsDto.builder()
                .accounts(accounts)
                .customer(convertToDTO(customer))
                .build();
    }

    @Override
    public CustomerDTO viewAuthenticatedCustomerDetails() {
        String username = CommonUtils.getLoggedInUsername();
        Customer customer = customerRepository.findByUserAuthProfile_Email(username)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        return convertToDTO(customer);
    }
    private CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setUsername(customer.getUserAuthProfile().getUsername());
        dto.setEmail(customer.getUserAuthProfile().getEmail());
        dto.setFirstName(customer.getUserAuthProfile().getFirstName());
        dto.setLastName(customer.getUserAuthProfile().getLastName());
        dto.setPhoneNumber(customer.getUserAuthProfile().getPhoneNumber());
        dto.setAddress(customer.getAddress());
        dto.setDateOfBirth(customer.getDateOfBirth());
        dto.setGender(customer.getGender());
        return dto;
    }

}
