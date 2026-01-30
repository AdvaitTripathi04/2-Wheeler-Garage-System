package com.garage.system.service;

import com.garage.system.model.Customer;
import com.garage.system.model.Vehicle;
import com.garage.system.repository.CustomerRepository;
import com.garage.system.repository.VehicleRepository;
import com.garage.system.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer saveCustomer(Customer customer) {
        Customer saved = customerRepository.save(customer);
        auditLogRepository.save(
                new com.garage.system.model.AuditLog("REGISTER_CUSTOMER",
                        "Registered customer: " + saved.getName()));
        return saved;
    }

    public Vehicle registerVehicle(Vehicle vehicle, String customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        vehicle.setCustomer(customer);
        return vehicleRepository.save(vehicle);
    }
}
