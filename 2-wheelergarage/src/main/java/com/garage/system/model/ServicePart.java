package com.garage.system.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "service_parts")
public class ServicePart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String partName;
    private Integer quantity;
    private Double unitCost;
    private Double totalCost;

    public ServicePart(String partName, Integer quantity, Double unitCost) {
        this.partName = partName;
        this.quantity = quantity;
        this.unitCost = unitCost;
        this.totalCost = quantity * unitCost;
    }
}
