package com.garage.system.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String action;
    private String details;
    private LocalDateTime timestamp;

    public AuditLog(String action, String details) {
        this.action = action;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }
}
