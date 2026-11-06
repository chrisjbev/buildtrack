package com.buildtrack.buildtrack;

import com.buildtrack.buildtrack.model.Customer;
import com.buildtrack.buildtrack.model.Project;
import com.buildtrack.buildtrack.model.Task;
import com.buildtrack.buildtrack.repository.CustomerRepository;
import com.buildtrack.buildtrack.repository.ProjectRepository;
import com.buildtrack.buildtrack.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TaskRepositoryIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void saveAndRetrieveTask() {

        Customer customer = new Customer(
                null,
                "Task Test Customer",
                "tasktest@example.com",
                "555-222-3333"
        );

        Customer savedCustomer = customerRepository.save(customer);

        Project project = new Project();
        project.setName("Bathroom Renovation");
        project.setDescription("Bathroom remodel");
        project.setCustomer(savedCustomer);

        Project savedProject = projectRepository.save(project);

        Task task = new Task();
        task.setName("Install Shower");
        task.setDescription("Install new shower");
        task.setProject(savedProject);

        Task savedTask = taskRepository.save(task);

        assertNotNull(savedTask.getId());

        Task retrievedTask = taskRepository
                .findById(savedTask.getId())
                .orElseThrow();

        assertEquals("Install Shower", retrievedTask.getName());
        assertEquals("Install new shower", retrievedTask.getDescription());
        assertEquals(savedProject.getId(), retrievedTask.getProject().getId());
    }
}
