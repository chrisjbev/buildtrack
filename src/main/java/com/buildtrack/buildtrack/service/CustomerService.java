package com.buildtrack.buildtrack.service;

import com.buildtrack.buildtrack.model.Customer;
import com.buildtrack.buildtrack.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    public Customer createCustomer(String name, String email, String phone) {
        Customer customer = new Customer(null, name, email, phone);
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, String name, String email, String phone) {
        Customer existingCustomer = getCustomerById(id);

        if (existingCustomer == null) {
            return null;
        }

        Customer updatedCustomer = new Customer(id, name, email, phone);
        return customerRepository.save(updatedCustomer);
    }

    public boolean deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            return false;
        }

        customerRepository.deleteById(id);
        return true;
    }
}
