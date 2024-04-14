package com.interswitchgroup.accountmanagementsystem.authentication.repository;
/**
 * @author Joy Osayi
 * @createdOn Apr-12(Fri)-2024
 */

import java.util.Optional;
import java.util.Set;

import com.interswitchgroup.accountmanagementsystem.authentication.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

  Optional<Permission> findFirstByNameOrderByCreatedDate(String name);

  Set<Permission> findAllByNameIn(Set<String> permissionNames);

  Set<Permission> findByPermissionType(String permissionType);

}
