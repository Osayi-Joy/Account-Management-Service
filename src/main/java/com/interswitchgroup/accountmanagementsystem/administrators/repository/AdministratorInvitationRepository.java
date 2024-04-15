package com.interswitchgroup.accountmanagementsystem.administrators.repository;

import com.interswitchgroup.accountmanagementsystem.administrators.model.AdministratorInvitation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Joy Osayi
 * @createdOn Apr-13(Sat)-2024
 */
@Repository
public interface AdministratorInvitationRepository extends JpaRepository<AdministratorInvitation, Long>, JpaSpecificationExecutor<AdministratorInvitation> {
    Optional<AdministratorInvitation> findByInvitationCode(String invitationCode);

    Optional<AdministratorInvitation> findByEmail(String email);
}