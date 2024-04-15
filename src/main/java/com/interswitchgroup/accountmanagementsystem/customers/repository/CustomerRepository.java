package com.interswitchgroup.accountmanagementsystem.customers.repository;
/**
 * @author Joy Osayi
 * @createdOn Apr-14(Sun)-2024
 */
import com.interswitchgroup.accountmanagementsystem.customers.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByUserAuthProfile_Email(String email);

    Optional<Customer> findByUserAuthProfile_Email(String username);
}

