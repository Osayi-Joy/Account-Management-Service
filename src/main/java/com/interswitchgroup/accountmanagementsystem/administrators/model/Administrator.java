package com.interswitchgroup.accountmanagementsystem.administrators.model;

import com.interswitchgroup.accountmanagementsystem.authentication.model.UserAuthProfile;
import com.interswitchgroup.accountmanagementsystem.common.audit.Auditable;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.*;

/**
 * @author Joy Osayi
 * @createdOn Apr-12(Fri)-2024
 */
@Entity
@Table(name = "administrators")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Administrator extends Auditable<String> implements Serializable {
    @OneToOne
    @JoinColumn(name = "user_auth_profile_id")
    private UserAuthProfile userAuthProfile;
   

}
