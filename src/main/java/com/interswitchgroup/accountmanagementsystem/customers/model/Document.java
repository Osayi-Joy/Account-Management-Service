package com.interswitchgroup.accountmanagementsystem.customers.model;
import com.interswitchgroup.accountmanagementsystem.common.audit.Auditable;
import com.interswitchgroup.accountmanagementsystem.common.enums.DocumentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author Joy Osayi
 * @createdOn Apr-14(Sun)-2024
 */
@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Document extends Auditable<String> implements Serializable {
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private DocumentType type;

    @Column(name = "document_number")
    private String documentNumber;

    private String issuer;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "file_path")
    private String filePath;

    private String description;
}
