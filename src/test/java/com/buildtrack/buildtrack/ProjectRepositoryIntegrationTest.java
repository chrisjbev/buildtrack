package com.buildtrack.buildtrack;

import com.buildtrack.buildtrack.model.Customer;
import com.buildtrack.buildtrack.model.Project;
import com.buildtrack.buildtrack.repository.CustomerRepository;
import com.buildtrack.buildtrack.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProjectRepositoryIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void saveAndRetrieveProject() {

        Customer customer = new Customer(
                null,
                "Test Customer",
                "projecttest@example.com",
                "555-111-2222"
        );

        Customer savedCustomer = customerRepository.save(customer);

        Project project = new Project();
        project.setName("Kitchen Renovation");
        project.setDescription("Complete kitchen renovation");
        project.setCustomer(savedCustomer);

        Project savedProject = projectRepository.save(project);

        assertNotNull(savedProject.getId());

        Project retrievedProject = projectRepository
                .findById(savedProject.getId())
                .orElseThrow();

        assertEquals("Kitchen Renovation", retrievedProject.getName());
        assertEquals("Complete kitchen renovation", retrievedProject.getDescription());
        assertEquals(savedCustomer.getId(), retrievedProject.getCustomer().getId());
    }
}
