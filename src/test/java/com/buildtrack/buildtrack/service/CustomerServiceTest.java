package com.buildtrack.buildtrack.service;

import com.buildtrack.buildtrack.model.Customer;
import com.buildtrack.buildtrack.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void getAllCustomersReturnsAllCustomers() {

        Customer customer = new Customer(
                1L,
                "John Smith",
                "john@example.com",
                "555-123-4567"
        );

        when(customerRepository.findAll()).thenReturn(List.of(customer));

        List<Customer> result = customerService.getAllCustomers();

        assertEquals(1, result.size());
        assertEquals("John Smith", result.get(0).getName());

        verify(customerRepository).findAll();
    }

    @Test
    void createCustomerSavesCustomer() {

        Customer savedCustomer = new Customer(
                1L,
                "Jane Doe",
                "jane@example.com",
                "555-987-6543"
        );

        when(customerRepository.save(any(Customer.class)))
                .thenReturn(savedCustomer);

        Customer result = customerService.createCustomer(
                "Jane Doe",
                "jane@example.com",
                "555-987-6543"
        );

        assertEquals(1L, result.getId());
        assertEquals("Jane Doe", result.getName());

        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void getCustomerByIdReturnsCustomer() {

        Customer customer = new Customer(
                1L,
                "John Smith",
                "john@example.com",
                "555-123-4567"
        );

        when(customerRepository.findById(1L))
                .thenReturn(java.util.Optional.of(customer));

        Customer result = customerService.getCustomerById(1L);

        assertEquals("John Smith", result.getName());
        assertEquals(1L, result.getId());

        verify(customerRepository).findById(1L);
    }

    @Test
    void getCustomerByIdReturnsNullWhenCustomerDoesNotExist() {

        when(customerRepository.findById(99L))
                .thenReturn(java.util.Optional.empty());

        Customer result = customerService.getCustomerById(99L);

        assertEquals(null, result);

        verify(customerRepository).findById(99L);
    }

    @Test
    void deleteCustomerReturnsTrueWhenCustomerExists() {

        when(customerRepository.existsById(1L)).thenReturn(true);

        boolean result = customerService.deleteCustomer(1L);

        assertEquals(true, result);

        verify(customerRepository).existsById(1L);
        verify(customerRepository).deleteById(1L);
    }

    @Test
    void deleteCustomerReturnsFalseWhenCustomerDoesNotExist() {

        when(customerRepository.existsById(99L)).thenReturn(false);

        boolean result = customerService.deleteCustomer(99L);

        assertEquals(false, result);

        verify(customerRepository).existsById(99L);
    }

}
