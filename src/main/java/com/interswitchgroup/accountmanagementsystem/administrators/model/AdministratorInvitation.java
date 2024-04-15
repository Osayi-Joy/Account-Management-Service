package com.interswitchgroup.accountmanagementsystem.administrators.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.interswitchgroup.accountmanagementsystem.common.audit.Auditable;
import com.interswitchgroup.accountmanagementsystem.common.enums.Status;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 * @author Joy Osayi
 * @createdOn Apr-13(Sat)-2024
 */
@AllArgsConstructor
@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "administrator_invitation")
@SQLDelete(sql = "UPDATE administrator_invitation SET invitation_status = 'DELETED' WHERE id=?")
@Where(clause = "invitation_status != 'DELETED'")
public class AdministratorInvitation extends Auditable<String> {

    @Column(name = "administrator_email", nullable = false, unique = true)
    private String email;

    private String role;

    @Enumerated(EnumType.STRING)
    private Status invitationStatus = Status.PENDING;

    @Column(nullable = false, unique = true)
    private String invitationCode;

    @Column(nullable = false)
    private boolean deleted;

    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;


}