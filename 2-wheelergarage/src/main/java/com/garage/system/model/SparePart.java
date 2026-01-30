package com.garage.system.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "spare_parts")
public class SparePart {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String partNumber;
    private Double price;
    private Integer stockQuantity;

}
