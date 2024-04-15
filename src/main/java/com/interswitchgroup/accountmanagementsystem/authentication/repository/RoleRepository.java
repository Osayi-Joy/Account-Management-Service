package com.interswitchgroup.accountmanagementsystem.authentication.repository;
/**
 * @author Joy Osayi
 * @createdOn Apr-12(Fri)-2024
 */


import java.util.List;
import java.util.Optional;

import com.interswitchgroup.accountmanagementsystem.authentication.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findFirstByNameOrderByCreatedDate(String name);
  Optional<Role> findFirstByNameAndIsDeletedFalseOrderByCreatedDate(String name);

  Page<Role> findAllByIsDeletedFalse(Pageable pageable);

  List<Role> findAllByIsDeletedFalse();

  boolean existsByName(String name);
}
