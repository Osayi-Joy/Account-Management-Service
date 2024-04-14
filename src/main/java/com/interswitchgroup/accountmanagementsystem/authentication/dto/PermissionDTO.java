package com.interswitchgroup.accountmanagementsystem.authentication.dto;


import java.io.Serializable;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PermissionDTO implements Serializable {
  @NonNull
  private String name;

  private String description;
  private String permissionType;
}
