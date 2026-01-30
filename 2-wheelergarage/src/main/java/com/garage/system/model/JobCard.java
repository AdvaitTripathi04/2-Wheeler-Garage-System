package com.garage.system.model;

import com.garage.system.model.ServicePart;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "job_cards")
public class JobCard {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    private String complaint;
    private String status;

    private String serviceType;
    private String mechanicName;

    private LocalDateTime entryDate;
    private LocalDateTime deliveryDate;

    private Double totalCost;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "job_card_id")
    private java.util.List<ServicePart> partsUsed = new java.util.ArrayList<>();
}
