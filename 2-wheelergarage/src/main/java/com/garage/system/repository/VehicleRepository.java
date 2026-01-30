package com.garage.system.repository;

import com.garage.system.model.Vehicle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    java.util.Optional<Vehicle> findByRegistrationNumber(String registrationNumber);

    @org.springframework.data.jpa.repository.Query("SELECT v FROM Vehicle v WHERE v.nextServiceDue <= :date")
    java.util.List<Vehicle> findDueForService(
            @org.springframework.data.repository.query.Param("date") java.time.LocalDateTime date);
}
