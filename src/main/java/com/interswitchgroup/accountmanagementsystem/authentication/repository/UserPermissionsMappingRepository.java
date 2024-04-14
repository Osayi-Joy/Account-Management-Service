package com.interswitchgroup.accountmanagementsystem.authentication.repository;
/**
 * @author Joy Osayi
 * @createdOn Apr-14(Sun)-2024
 */
import com.interswitchgroup.accountmanagementsystem.authentication.model.UserPermissionsMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPermissionsMappingRepository extends JpaRepository<UserPermissionsMapping, Long> {

}
