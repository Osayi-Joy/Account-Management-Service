package com.interswitchgroup.accountmanagementsystem.authentication.dto.request;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoleCreationDTO implements Serializable {
 @NonNull
 private String name;
 @NonNull
 private String description;
 private Set<String> permissions = new HashSet<>();
}
