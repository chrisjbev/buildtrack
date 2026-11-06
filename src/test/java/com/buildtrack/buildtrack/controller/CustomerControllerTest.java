package com.buildtrack.buildtrack.controller;

import com.buildtrack.buildtrack.model.Customer;
import com.buildtrack.buildtrack.model.Project;
import com.buildtrack.buildtrack.service.CustomerService;
import com.buildtrack.buildtrack.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    void getAllCustomersReturnsCustomers() throws Exception {
        Customer customer = new Customer(1L, "John Smith",
                "john@example.com", "555-123-4567");

        when(customerService.getAllCustomers()).thenReturn(List.of(customer));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Smith"));
    }

    @Test
    void getCustomerByIdReturnsCustomer() throws Exception {
        Customer customer = new Customer(1L, "John Smith",
                "john@example.com", "555-123-4567");

        when(customerService.getCustomerById(1L)).thenReturn(customer);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Smith"));
    }

    @Test
    void getCustomerByIdReturnsNotFound() throws Exception {
        when(customerService.getCustomerById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/customers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getProjectsByCustomerIdReturnsProjects() throws Exception {
        Project project = new Project(
                1L, "Kitchen Remodel", "Full kitchen renovation",
                "123 Main Street, New Jersey", null, null, null
        );

        when(projectService.getProjectsByCustomerId(1L))
                .thenReturn(List.of(project));

        mockMvc.perform(get("/api/customers/1/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Kitchen Remodel"));
    }

    @Test
    void createCustomerReturnsCreatedCustomer() throws Exception {
        Customer customer = new Customer(2L, "Mike Johnson",
                "mike@example.com", "555-222-3333");

        when(customerService.createCustomer(
                "Mike Johnson", "mike@example.com", "555-222-3333"))
                .thenReturn(customer);

        mockMvc.perform(post("/api/customers")
                        .contentType("application/json")
                        .content("""
                            {
                                "name": "Mike Johnson",
                                "email": "mike@example.com",
                                "phone": "555-222-3333"
                            }
                            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Mike Johnson"));
    }

    @Test
    void createCustomerWithInvalidDataReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/customers")
                        .contentType("application/json")
                        .content("""
                            {
                                "name": "",
                                "email": "invalid-email",
                                "phone": "555-222-3333"
                            }
                            """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCustomerReturnsUpdatedCustomer() throws Exception {
        Customer customer = new Customer(1L, "John Updated",
                "johnupdated@example.com", "555-999-9999");

        when(customerService.updateCustomer(
                1L, "John Updated", "johnupdated@example.com", "555-999-9999"))
                .thenReturn(customer);

        mockMvc.perform(put("/api/customers/1")
                        .contentType("application/json")
                        .content("""
                            {
                                "name": "John Updated",
                                "email": "johnupdated@example.com",
                                "phone": "555-999-9999"
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Updated"));
    }

    @Test
    void updateCustomerReturnsNotFound() throws Exception {
        when(customerService.updateCustomer(
                99L, "John Updated", "johnupdated@example.com", "555-999-9999"))
                .thenReturn(null);

        mockMvc.perform(put("/api/customers/99")
                        .contentType("application/json")
                        .content("""
                            {
                                "name": "John Updated",
                                "email": "johnupdated@example.com",
                                "phone": "555-999-9999"
                            }
                            """))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCustomerReturnsNoContent() throws Exception {
        when(customerService.deleteCustomer(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCustomerReturnsNotFound() throws Exception {
        when(customerService.deleteCustomer(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/customers/99"))
                .andExpect(status().isNotFound());
    }
}