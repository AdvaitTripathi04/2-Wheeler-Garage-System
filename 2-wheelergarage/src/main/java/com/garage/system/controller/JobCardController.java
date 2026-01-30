package com.garage.system.controller;

import com.garage.system.model.JobCard;
import com.garage.system.model.Vehicle;
import com.garage.system.service.JobCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-cards")
public class JobCardController {

    @Autowired
    private JobCardService jobCardService;

    @GetMapping
    public List<JobCard> getAllJobCards() {
        return jobCardService.getAllJobCards();
    }

    @PostMapping
    public JobCard createJobCard(@RequestBody JobCard jobCard, @RequestParam String vehicleId) {
        return jobCardService.createJobCard(
                vehicleId,
                jobCard.getComplaint(),
                jobCard.getServiceType(),
                jobCard.getMechanicName(),
                jobCard.getPartsUsed());
    }

    @GetMapping("/search-vehicle")
    public Vehicle findVehicle(@RequestParam String regNumber) {
        return jobCardService.findVehicleByReg(regNumber);
    }

    @PutMapping("/{id}/complete")
    public JobCard completeJob(@PathVariable String id) {
        return jobCardService.completeJob(id);
    }
}
