package com.interswitchgroup.accountmanagementsystem.authentication.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO implements Serializable {
  @NonNull
  private String name;
  @NonNull
  private String description;
  private Set<PermissionDTO> permissions = new HashSet<>();
}
