package com.interswitchgroup.accountmanagementsystem.authentication.model;

import com.interswitchgroup.accountmanagementsystem.common.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/**
 * @author Joy Osayi
 * @createdOn Apr-14(Sun)-2024
 */


@Entity
@Table(name = "user_permissions_mapping")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPermissionsMapping extends Auditable<String> implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_auth_profile_id")
    private UserAuthProfile userAuthProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id")
    private Permission permission;
}
