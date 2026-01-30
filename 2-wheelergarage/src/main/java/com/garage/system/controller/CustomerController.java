package com.garage.system.controller;

import com.garage.system.model.Customer;
import com.garage.system.model.Vehicle;
import com.garage.system.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.saveCustomer(customer);
    }

    @PostMapping("/{customerId}/vehicles")
    public Vehicle addVehicle(@PathVariable String customerId, @RequestBody Vehicle vehicle) {
        return customerService.registerVehicle(vehicle, customerId);
    }
}
