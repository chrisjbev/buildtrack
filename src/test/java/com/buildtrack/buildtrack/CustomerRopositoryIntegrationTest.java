package com.buildtrack.buildtrack;

import com.buildtrack.buildtrack.model.Customer;
import com.buildtrack.buildtrack.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CustomerRepositoryIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void saveAndRetrieveCustomer() {

        Customer customer = new Customer(
                null,
                "Integration Test Customer",
                "integration@example.com",
                "555-123-4567"
        );

        Customer savedCustomer = customerRepository.save(customer);

        assertNotNull(savedCustomer.getId());

        Customer retrievedCustomer = customerRepository
                .findById(savedCustomer.getId())
                .orElseThrow();

        assertEquals("Integration Test Customer", retrievedCustomer.getName());
        assertEquals("integration@example.com", retrievedCustomer.getEmail());
        assertEquals("555-123-4567", retrievedCustomer.getPhone());
    }
}