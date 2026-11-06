package com.buildtrack.buildtrack.controller;

import com.buildtrack.buildtrack.model.Project;
import com.buildtrack.buildtrack.model.ProjectStatus;
import com.buildtrack.buildtrack.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(projectController).build();
    }

    @Test
    void getAllProjectsReturnsProjects() throws Exception {
        Project project = new Project(
                1L,
                "Kitchen Remodel",
                "Full kitchen renovation",
                "123 Main Street, New Jersey",
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2027, 1, 15),
                ProjectStatus.IN_PROGRESS
        );

        when(projectService.getAllProjects()).thenReturn(List.of(project));

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Kitchen Remodel"));
    }

    @Test
    void getProjectByIdReturnsProject() throws Exception {
        Project project = new Project(
                1L,
                "Kitchen Remodel",
                "Full kitchen renovation",
                "123 Main Street, New Jersey",
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2027, 1, 15),
                ProjectStatus.IN_PROGRESS
        );

        when(projectService.getProjectById(1L)).thenReturn(project);

        mockMvc.perform(get("/api/projects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Kitchen Remodel"));
    }

    @Test
    void getProjectByIdReturnsNotFound() throws Exception {
        when(projectService.getProjectById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/projects/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createProjectForCustomerReturnsCreatedProject() throws Exception {
        Project project = new Project(
                2L,
                "Bathroom Renovation",
                "Complete bathroom renovation",
                "123 Main Street, New Jersey",
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2027, 1, 15),
                ProjectStatus.PLANNING
        );

        when(projectService.createProjectForCustomer(eq(1L), any(Project.class)))
                .thenReturn(project);

        mockMvc.perform(post("/api/projects/customer/1")
                        .contentType("application/json")
                        .content("""
                            {
                                "name": "Bathroom Renovation",
                                "description": "Complete bathroom renovation",
                                "address": "123 Main Street, New Jersey",
                                "startDate": "2026-11-10",
                                "expectedCompletionDate": "2027-01-15",
                                "status": "PLANNING"
                            }
                            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Bathroom Renovation"));
    }

    @Test
    void createProjectForCustomerReturnsNotFound() throws Exception {
        when(projectService.createProjectForCustomer(eq(99L), any(Project.class)))
                .thenReturn(null);

        mockMvc.perform(post("/api/projects/customer/99")
                        .contentType("application/json")
                        .content("""
                            {
                                "name": "Bathroom Renovation",
                                "description": "Complete bathroom renovation",
                                "address": "123 Main Street, New Jersey",
                                "startDate": "2026-11-10",
                                "expectedCompletionDate": "2027-01-15",
                                "status": "PLANNING"
                            }
                            """))
                .andExpect(status().isNotFound());
    }

    @Test
    void createProjectWithInvalidDataReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/projects/customer/1")
                        .contentType("application/json")
                        .content("""
                            {
                                "name": "",
                                "description": "Complete bathroom renovation",
                                "address": "123 Main Street, New Jersey",
                                "startDate": "2026-11-10",
                                "expectedCompletionDate": "2027-01-15",
                                "status": "PLANNING"
                            }
                            """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateProjectReturnsUpdatedProject() throws Exception {
        Project project = new Project(
                1L,
                "Updated Kitchen Remodel",
                "Updated kitchen renovation",
                "123 Main Street, New Jersey",
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2027, 3, 15),
                ProjectStatus.IN_PROGRESS
        );

        when(projectService.updateProject(eq(1L), any(Project.class)))
                .thenReturn(project);

        mockMvc.perform(put("/api/projects/1")
                        .contentType("application/json")
                        .content("""
                            {
                                "name": "Updated Kitchen Remodel",
                                "description": "Updated kitchen renovation",
                                "address": "123 Main Street, New Jersey",
                                "startDate": "2026-11-10",
                                "expectedCompletionDate": "2027-03-15",
                                "status": "IN_PROGRESS"
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Kitchen Remodel"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void updateProjectReturnsNotFound() throws Exception {
        when(projectService.updateProject(eq(99L), any(Project.class)))
                .thenReturn(null);

        mockMvc.perform(put("/api/projects/99")
                        .contentType("application/json")
                        .content("""
                            {
                                "name": "Updated Project",
                                "description": "Updated description",
                                "address": "123 Main Street, New Jersey",
                                "startDate": "2026-11-10",
                                "expectedCompletionDate": "2027-03-15",
                                "status": "IN_PROGRESS"
                            }
                            """))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProjectReturnsNoContent() throws Exception {
        when(projectService.deleteProject(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/projects/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteProjectReturnsNotFound() throws Exception {
        when(projectService.deleteProject(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/projects/99"))
                .andExpect(status().isNotFound());
    }
}
