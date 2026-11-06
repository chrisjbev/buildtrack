package com.buildtrack.buildtrack.controller;

import com.buildtrack.buildtrack.model.Task;
import com.buildtrack.buildtrack.service.TaskService;
import com.buildtrack.buildtrack.dto.TaskRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@SecurityRequirement(name = "bearerAuth")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);

        if (task == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(task);
    }

    @GetMapping("/project/{projectId}")
    public List<Task> getTasksByProjectId(
            @PathVariable Long projectId) {
        return taskService.getTasksByProjectId(projectId);
    }

    @PostMapping("/project/{projectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Task> createTaskForProject(
            @PathVariable Long projectId,
            @Valid @RequestBody TaskRequest request) {

        Task task = new Task(
                null,
                request.getName(),
                request.getDescription(),
                request.getStatus(),
                request.getDueDate(),
                null
        );

        Task createdTask = taskService.createTaskForProject(
                projectId, task
        );

        if (createdTask == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(201).body(createdTask);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request) {

        Task task = new Task(
                null,
                request.getName(),
                request.getDescription(),
                request.getStatus(),
                request.getDueDate(),
                null
        );

        Task updatedTask = taskService.updateTask(id, task);

        if (updatedTask == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        boolean deleted = taskService.deleteTask(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}