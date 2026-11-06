package com.buildtrack.buildtrack.repository;

import com.buildtrack.buildtrack.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByCustomerId(Long customerId);
}