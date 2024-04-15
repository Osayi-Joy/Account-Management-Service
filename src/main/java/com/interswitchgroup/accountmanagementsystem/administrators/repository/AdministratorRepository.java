package com.interswitchgroup.accountmanagementsystem.administrators.repository;

import com.interswitchgroup.accountmanagementsystem.administrators.model.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Joy Osayi
 * @createdOn Apr-12(Fri)-2024
 */
@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Long>, JpaSpecificationExecutor<Administrator> {

    boolean existsByUserAuthProfile_Email(String superAdminEmail);

    Optional<Administrator> findByUserAuthProfile_Email(String username);
}
