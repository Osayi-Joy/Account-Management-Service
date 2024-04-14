package com.interswitchgroup.accountmanagementsystem.authentication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.interswitchgroup.accountmanagementsystem.common.enums.Status;
import com.interswitchgroup.accountmanagementsystem.users.model.UserProfile;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

/**
 * @author Joy Osayi
 * @createdOn Apr-12(Fri)-2024
 */
@Entity
@Table(name = "user_auth_profile", indexes = @Index(columnList = "username"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthProfile extends UserProfile implements Serializable {

    private String assignedRole;

    @OneToMany(mappedBy = "userAuthProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<UserPermissionsMapping> userPermissionsMappings = new HashSet<>();

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private Status status = Status.INACTIVE;


}
