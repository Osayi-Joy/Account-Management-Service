package com.interswitchgroup.accountmanagementsystem.authentication.model;

import com.interswitchgroup.accountmanagementsystem.common.audit.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Joy Osayi
 * @createdOn Apr-12(Fri)-2024
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Role extends Auditable<String> implements Serializable {

    @Column(nullable = false)
    private String name;
    private String description;

    @Column(nullable = false)
    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "roles_and_permissions_mapping",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id", referencedColumnName = "id")})
    @ToString.Exclude
    private Set<Permission> permissions = new HashSet<>();
}
