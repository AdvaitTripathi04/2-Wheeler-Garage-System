package com.garage.system.repository;

import com.garage.system.model.SparePart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface SparePartRepository extends JpaRepository<SparePart, Long> {
}
