package com.interswitchgroup.accountmanagementsystem.authentication.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.interswitchgroup.accountmanagementsystem.common.audit.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Joy Osayi
 * @createdOn Apr-12(Fri)-2024
 */
@Entity
@Table(name = "permissions")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Permission extends Auditable<String> implements Serializable{

    @Column(nullable = false, unique = true)
    private String name;
    private String description;
    private String permissionType;
}
