package com.interswitchgroup.accountmanagementsystem.authentication.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.interswitchgroup.accountmanagementsystem.users.dto.UserProfileDto;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class UserAuthProfileDTO extends UserProfileDto implements UserDetails, Serializable {
  private String assignedRole;
  private String password;

  private Set<SimpleGrantedAuthority> permissions;
  private Set<PermissionDTO> userPermissions;
  private LocalDateTime lastPasswordUpdatedDate;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.permissions;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
