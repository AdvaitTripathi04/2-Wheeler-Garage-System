package com.garage.system.service;

import com.garage.system.model.JobCard;
import com.garage.system.model.ServicePart;
import com.garage.system.model.Vehicle;
import com.garage.system.repository.JobCardRepository;
import com.garage.system.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobCardService {

    @Autowired
    private JobCardRepository jobCardRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    public JobCard createJobCard(String vehicleId, String complaint, String serviceType, String mechanicName,
            List<ServicePart> parts) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        JobCard jobCard = new JobCard();
        jobCard.setVehicle(vehicle);
        jobCard.setComplaint(complaint);
        jobCard.setServiceType(serviceType);
        jobCard.setMechanicName(mechanicName);
        jobCard.setEntryDate(LocalDateTime.now());
        jobCard.setPartsUsed(parts);

        double total = parts.stream().mapToDouble(ServicePart::getTotalCost).sum();
        // Add a base service charge if needed
        jobCard.setTotalCost(total);

        jobCard.setStatus("PENDING");
        return jobCardRepository.save(jobCard);
    }

    public Vehicle findVehicleByReg(String regNumber) {
        return vehicleRepository.findByRegistrationNumber(regNumber)
                .orElseThrow(() -> new RuntimeException("Vehicle with Reg Number " + regNumber + " not found"));
    }

    public List<JobCard> getAllJobCards() {
        return jobCardRepository.findAll();
    }

    public JobCard completeJob(String jobId) {
        JobCard job = jobCardRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job card not found"));

        job.setStatus("COMPLETED");
        job.setDeliveryDate(LocalDateTime.now());

        // Update Vehicle service dates
        Vehicle vehicle = job.getVehicle();
        if (vehicle != null) {
            vehicle.setLastServiceDate(job.getDeliveryDate());
            // Calculate next service due date (3 months from now)
            vehicle.setNextServiceDue(job.getDeliveryDate().plusMonths(3));
            vehicleRepository.save(vehicle);
        }

        return jobCardRepository.save(job);
    }
}
