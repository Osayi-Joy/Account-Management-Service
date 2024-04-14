package com.interswitchgroup.accountmanagementsystem.authentication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.interswitchgroup.accountmanagementsystem.common.enums.Status;
import com.interswitchgroup.accountmanagementsystem.users.model.UserProfile;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_auth_profiles_and_permissions_mapping",
            joinColumns =
            @JoinColumn(name = "user_auth_profile_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"))
    @ToString.Exclude
    private Set<Permission> permissions = new HashSet<>();

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private Status status = Status.INACTIVE;


}
