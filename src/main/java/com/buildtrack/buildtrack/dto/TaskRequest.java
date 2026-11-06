package com.buildtrack.buildtrack.dto;

import com.buildtrack.buildtrack.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class TaskRequest {

    @NotBlank(message = "Task name is required")
    @Size(max = 150, message = "Task name cannot exceed 150 characters")
    private String name;

    private String description;

    @NotNull(message = "Task status is required")
    private TaskStatus status;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    public String getName() { return name; }
    public String getDescription() { return description; }
    public TaskStatus getStatus() { return status; }
    public LocalDate getDueDate() { return dueDate; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setStatus(TaskStatus status) { this.status = status; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}