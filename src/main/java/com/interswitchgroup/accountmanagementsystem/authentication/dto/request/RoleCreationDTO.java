package com.interswitchgroup.accountmanagementsystem.authentication.dto.request;


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
public class RoleCreationDTO implements Serializable {
 @NonNull
 private String name;
 @NonNull
 private String description;
 private Set<String> permissions = new HashSet<>();
}
