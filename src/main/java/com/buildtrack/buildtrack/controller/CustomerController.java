package com.buildtrack.buildtrack.controller;

import com.buildtrack.buildtrack.dto.CustomerRequest;
import com.buildtrack.buildtrack.model.Customer;
import com.buildtrack.buildtrack.model.Project;
import com.buildtrack.buildtrack.service.CustomerService;
import com.buildtrack.buildtrack.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@SecurityRequirement(name = "bearerAuth")
public class CustomerController {

    private final CustomerService customerService;
    private final ProjectService projectService;

    public CustomerController(CustomerService customerService,
                              ProjectService projectService) {
        this.customerService = customerService;
        this.projectService = projectService;
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{customerId}/projects")
    public List<Project> getProjectsByCustomerId(
            @PathVariable Long customerId) {
        return projectService.getProjectsByCustomerId(customerId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);

        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(customer);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Customer> createCustomer(
            @Valid @RequestBody CustomerRequest request) {

        Customer createdCustomer = customerService.createCustomer(
                request.getName(),
                request.getEmail(),
                request.getPhone()
        );

        return ResponseEntity.status(201).body(createdCustomer);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Customer> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request) {

        Customer updatedCustomer = customerService.updateCustomer(
                id,
                request.getName(),
                request.getEmail(),
                request.getPhone()
        );

        if (updatedCustomer == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        boolean deleted = customerService.deleteCustomer(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}

