package com.garage.system.service;

import com.garage.system.model.Vehicle;
import com.garage.system.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public List<Vehicle> getVehiclesDueForService() {
        // Find vehicles where next service due is anytime in the past or in the next 7
        // days
        return vehicleRepository.findDueForService(LocalDateTime.now().plusDays(7));
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }
}
