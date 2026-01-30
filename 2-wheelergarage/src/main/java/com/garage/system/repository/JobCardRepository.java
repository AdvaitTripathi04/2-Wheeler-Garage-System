package com.garage.system.repository;

import com.garage.system.model.JobCard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobCardRepository extends JpaRepository<JobCard, String> {
    List<JobCard> findByStatus(String status);
}
