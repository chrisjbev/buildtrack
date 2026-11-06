package com.buildtrack.buildtrack.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String address;
    private LocalDate startDate;
    private LocalDate expectedCompletionDate;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Project() {
    }

    public Project(Long id, String name, String description,
                   String address, LocalDate startDate,
                   LocalDate expectedCompletionDate, ProjectStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.startDate = startDate;
        this.expectedCompletionDate = expectedCompletionDate;
        this.status = status;
    }

    public Project(Long id, String name, String description,
                   String address, LocalDate startDate,
                   LocalDate expectedCompletionDate, ProjectStatus status,
                   Customer customer) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.startDate = startDate;
        this.expectedCompletionDate = expectedCompletionDate;
        this.status = status;
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

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

    public Customer getCustomer() {
        return customer;
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

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}