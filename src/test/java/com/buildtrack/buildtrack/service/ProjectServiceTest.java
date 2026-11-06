package com.buildtrack.buildtrack.service;

import com.buildtrack.buildtrack.model.Project;
import com.buildtrack.buildtrack.model.ProjectStatus;
import com.buildtrack.buildtrack.repository.CustomerRepository;
import com.buildtrack.buildtrack.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.buildtrack.buildtrack.model.Customer;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void getProjectByIdReturnsProject() {
        Project project = new Project(
                1L,
                "Kitchen Remodel",
                "Kitchen renovation",
                "123 Main Street",
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2027, 2, 10),
                ProjectStatus.PLANNING
        );

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        Project result = projectService.getProjectById(1L);

        assertNotNull(result);
        assertEquals("Kitchen Remodel", result.getName());
        verify(projectRepository).findById(1L);
    }

    @Test
    void getProjectByIdReturnsNullWhenProjectDoesNotExist() {

        when(projectRepository.findById(99L))
                .thenReturn(Optional.empty());

        Project result = projectService.getProjectById(99L);

        assertNull(result);

        verify(projectRepository).findById(99L);
    }

    @Test
    void createProjectForCustomerSavesProject() {

        Customer customer = new Customer(
                1L,
                "John Smith",
                "john@example.com",
                "555-123-4567"
        );

        Project project = new Project(
                null,
                "Bathroom Renovation",
                "Complete bathroom renovation",
                "456 Oak Street",
                LocalDate.of(2026, 12, 1),
                LocalDate.of(2027, 3, 1),
                ProjectStatus.PLANNING
        );

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        when(projectRepository.save(any(Project.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Project result = projectService.createProjectForCustomer(1L, project);

        assertNotNull(result);
        assertEquals("Bathroom Renovation", result.getName());
        assertEquals(customer, result.getCustomer());

        verify(customerRepository).findById(1L);
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void createProjectForCustomerReturnsNullWhenCustomerDoesNotExist() {

        Project project = new Project(
                null,
                "Bathroom Renovation",
                "Complete bathroom renovation",
                "456 Oak Street",
                LocalDate.of(2026, 12, 1),
                LocalDate.of(2027, 3, 1),
                ProjectStatus.PLANNING
        );

        when(customerRepository.findById(99L))
                .thenReturn(Optional.empty());

        Project result = projectService.createProjectForCustomer(99L, project);

        assertNull(result);

        verify(customerRepository).findById(99L);
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    void getProjectsByCustomerIdReturnsCustomerProjects() {

        Project project1 = new Project(
                1L,
                "Kitchen Remodel",
                "Kitchen renovation",
                "123 Main Street",
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2027, 2, 10),
                ProjectStatus.PLANNING
        );

        Project project2 = new Project(
                2L,
                "Bathroom Renovation",
                "Bathroom renovation",
                "456 Oak Street",
                LocalDate.of(2026, 12, 1),
                LocalDate.of(2027, 3, 1),
                ProjectStatus.PLANNING
        );

        when(projectRepository.findByCustomerId(1L))
                .thenReturn(List.of(project1, project2));

        List<Project> result = projectService.getProjectsByCustomerId(1L);

        assertEquals(2, result.size());
        assertEquals("Kitchen Remodel", result.get(0).getName());
        assertEquals("Bathroom Renovation", result.get(1).getName());

        verify(projectRepository).findByCustomerId(1L);
    }

    @Test
    void deleteProjectReturnsTrueWhenProjectExists() {

        when(projectRepository.existsById(1L)).thenReturn(true);

        boolean result = projectService.deleteProject(1L);

        assertTrue(result);

        verify(projectRepository).existsById(1L);
        verify(projectRepository).deleteById(1L);
    }

    @Test
    void deleteProjectReturnsFalseWhenProjectDoesNotExist() {

        when(projectRepository.existsById(99L)).thenReturn(false);

        boolean result = projectService.deleteProject(99L);

        assertFalse(result);

        verify(projectRepository).existsById(99L);
        verify(projectRepository, never()).deleteById(99L);
    }

    @Test
    void updateProjectReturnsUpdatedProject() {

        Project existingProject = new Project(
                1L,
                "Kitchen Remodel",
                "Old description",
                "123 Main Street",
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2027, 2, 10),
                ProjectStatus.PLANNING
        );

        Project updatedProject = new Project(
                null,
                "Kitchen Remodel",
                "Updated kitchen renovation",
                "123 Main Street",
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2027, 3, 10),
                ProjectStatus.IN_PROGRESS
        );

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(existingProject));

        when(projectRepository.save(any(Project.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Project result = projectService.updateProject(1L, updatedProject);

        assertNotNull(result);
        assertEquals("Updated kitchen renovation", result.getDescription());
        assertEquals(ProjectStatus.IN_PROGRESS, result.getStatus());

        verify(projectRepository).findById(1L);
        verify(projectRepository).save(any(Project.class));
    }
}