package com.buildtrack.buildtrack.dto;

import com.buildtrack.buildtrack.model.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class ProjectRequest {

    @NotBlank(message = "Project name is required")
    @Size(max = 150, message = "Project name cannot exceed 150 characters")
    private String name;

    private String description;

    @NotBlank(message = "Project address is required")
    private String address;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "Expected completion date is required")
    private LocalDate expectedCompletionDate;

    @NotNull(message = "Project status is required")
    private ProjectStatus status;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getExpectedCompletionDate() {
        return expectedCompletionDate;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setExpectedCompletionDate(LocalDate expectedCompletionDate) {
        this.expectedCompletionDate = expectedCompletionDate;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }
}
