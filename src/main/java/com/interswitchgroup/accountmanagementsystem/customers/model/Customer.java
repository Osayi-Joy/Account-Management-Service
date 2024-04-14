package com.interswitchgroup.accountmanagementsystem.customers.model;

import com.interswitchgroup.accountmanagementsystem.authentication.model.UserAuthProfile;
import com.interswitchgroup.accountmanagementsystem.common.audit.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Joy Osayi
 * @createdOn Apr-13(Sat)-2024
 */
@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends Auditable<String> implements Serializable{
    @OneToOne
    @JoinColumn(name = "user_auth_profile_id")
    private UserAuthProfile userAuthProfile;
}


