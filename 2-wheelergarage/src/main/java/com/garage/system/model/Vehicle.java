package com.garage.system.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String registrationNumber;
    private String brand;
    private String model;
    private String color;
    private Integer year;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private java.time.LocalDateTime lastServiceDate;
    private java.time.LocalDateTime nextServiceDue;
}
