package com.interswitchgroup.accountmanagementsystem.authentication.repository;

import com.interswitchgroup.accountmanagementsystem.authentication.model.UserAuthProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Joy Osayi
 * @createdOn Apr-12(Fri)-2024
 */
@Repository
public interface UserAuthProfileRepository extends JpaRepository<UserAuthProfile, Long> {

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<UserAuthProfile> findFirstByUsernameOrderByCreatedDate(String username);

}
