package com.buildtrack.buildtrack.service;

import com.buildtrack.buildtrack.model.Project;
import com.buildtrack.buildtrack.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import com.buildtrack.buildtrack.model.Customer;
import com.buildtrack.buildtrack.repository.CustomerRepository;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CustomerRepository customerRepository;

    public ProjectService(ProjectRepository projectRepository,
                          CustomerRepository customerRepository) {
        this.projectRepository = projectRepository;
        this.customerRepository = customerRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<Project> getProjectsByCustomerId(Long customerId) {
        return projectRepository.findByCustomerId(customerId);
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Project createProjectForCustomer(Long customerId, Project project) {

        Customer customer = customerRepository.findById(customerId).orElse(null);

        if (customer == null) {
            return null;
        }

        Project newProject = new Project(
                null,
                project.getName(),
                project.getDescription(),
                project.getAddress(),
                project.getStartDate(),
                project.getExpectedCompletionDate(),
                project.getStatus(),
                customer
        );

        return projectRepository.save(newProject);
    }

    public Project updateProject(Long id, Project project) {

        Project existingProject = getProjectById(id);

        if (existingProject == null) {
            return null;
        }

        existingProject.setName(project.getName());
        existingProject.setDescription(project.getDescription());
        existingProject.setAddress(project.getAddress());
        existingProject.setStartDate(project.getStartDate());
        existingProject.setExpectedCompletionDate(project.getExpectedCompletionDate());
        existingProject.setStatus(project.getStatus());

        return projectRepository.save(existingProject);
    }

    public boolean deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            return false;
        }

        projectRepository.deleteById(id);
        return true;
    }
}
