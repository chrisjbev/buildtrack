package com.buildtrack.buildtrack.controller;

import com.buildtrack.buildtrack.model.Project;
import com.buildtrack.buildtrack.service.ProjectService;
import com.buildtrack.buildtrack.dto.ProjectRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@SecurityRequirement(name = "bearerAuth")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);

        if (project == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(project);
    }

    @PostMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Project> createProjectForCustomer(
            @PathVariable Long customerId,
            @Valid @RequestBody ProjectRequest request) {

        Project project = new Project(
                null,
                request.getName(),
                request.getDescription(),
                request.getAddress(),
                request.getStartDate(),
                request.getExpectedCompletionDate(),
                request.getStatus()
        );

        Project createdProject = projectService.createProjectForCustomer(
                customerId, project
        );

        if (createdProject == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(201).body(createdProject);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Project> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request) {

        Project project = new Project(
                null,
                request.getName(),
                request.getDescription(),
                request.getAddress(),
                request.getStartDate(),
                request.getExpectedCompletionDate(),
                request.getStatus()
        );

        Project updatedProject = projectService.updateProject(id, project);

        if (updatedProject == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        boolean deleted = projectService.deleteProject(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}