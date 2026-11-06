package com.buildtrack.buildtrack.service;

import com.buildtrack.buildtrack.model.Customer;
import com.buildtrack.buildtrack.model.Project;
import com.buildtrack.buildtrack.model.ProjectStatus;
import com.buildtrack.buildtrack.model.Role;
import com.buildtrack.buildtrack.model.Task;
import com.buildtrack.buildtrack.model.TaskStatus;
import com.buildtrack.buildtrack.model.User;
import com.buildtrack.buildtrack.repository.CustomerRepository;
import com.buildtrack.buildtrack.repository.ProjectRepository;
import com.buildtrack.buildtrack.repository.TaskRepository;
import com.buildtrack.buildtrack.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Loads sample data so reviewers can try the API immediately.
 * Enabled with DEMO_DATA=true. Safe to run on every startup: sample records are
 * only added when the customers table is empty, and the demo user only if missing.
 */
@Component
@ConditionalOnProperty(name = "app.demo.enabled", havingValue = "true")
public class DemoDataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoDataSeeder.class);

    private final CustomerRepository customerRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String demoUsername;
    private final String demoPassword;

    public DemoDataSeeder(
            CustomerRepository customerRepository,
            ProjectRepository projectRepository,
            TaskRepository taskRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.demo.username:demo}") String demoUsername,
            @Value("${app.demo.password:DemoPass123!}") String demoPassword) {

        this.customerRepository = customerRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.demoUsername = demoUsername;
        this.demoPassword = demoPassword;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seedDemoUser();
        seedSampleData();
    }

    private void seedDemoUser() {
        if (userRepository.existsByUsername(demoUsername)) {
            return;
        }
        userRepository.save(new User(
                null,
                demoUsername,
                demoUsername + "@buildtrack.demo",
                passwordEncoder.encode(demoPassword),
                Role.USER));
        log.info("Created read-only demo user '{}'", demoUsername);
    }

    private void seedSampleData() {
        if (customerRepository.count() > 0) {
            return;
        }

        LocalDate today = LocalDate.now();

        Customer smith = customerRepository.save(
                new Customer(null, "Smith Family", "smith@example.com", "555-201-1001"));
        Customer riverside = customerRepository.save(
                new Customer(null, "Riverside Dental Group", "office@riversidedental.example", "555-201-2002"));
        Customer oakview = customerRepository.save(
                new Customer(null, "Oakview Property Management", "projects@oakview.example", "555-201-3003"));

        Project kitchen = projectRepository.save(new Project(null,
                "Kitchen Remodel", "Full kitchen renovation with new cabinets and island",
                "123 Maple St", today.minusWeeks(3), today.plusWeeks(5),
                ProjectStatus.IN_PROGRESS, smith));
        Project deck = projectRepository.save(new Project(null,
                "Backyard Deck", "400 sq ft composite deck with railing",
                "123 Maple St", today.plusWeeks(6), today.plusWeeks(9),
                ProjectStatus.PLANNING, smith));
        Project office = projectRepository.save(new Project(null,
                "Office Expansion", "Add two treatment rooms and a reception area",
                "88 River Rd, Suite 200", today.minusMonths(2), today.plusMonths(1),
                ProjectStatus.IN_PROGRESS, riverside));
        Project roof = projectRepository.save(new Project(null,
                "Roof Replacement", "Tear-off and new architectural shingles, Building B",
                "450 Oakview Blvd", today.minusMonths(4), today.minusMonths(3),
                ProjectStatus.COMPLETED, oakview));

        taskRepository.save(new Task(null, "Demolition",
                "Remove old cabinets, countertops and flooring",
                TaskStatus.COMPLETED, today.minusWeeks(2), kitchen));
        taskRepository.save(new Task(null, "Electrical rough-in",
                "Add circuits for island and under-cabinet lighting",
                TaskStatus.IN_PROGRESS, today.plusDays(3), kitchen));
        taskRepository.save(new Task(null, "Cabinet installation",
                "Install base and wall cabinets",
                TaskStatus.NOT_STARTED, today.plusWeeks(2), kitchen));
        taskRepository.save(new Task(null, "Permit application",
                "Submit deck plans to the city",
                TaskStatus.NOT_STARTED, today.plusWeeks(1), deck));
        taskRepository.save(new Task(null, "Framing",
                "Frame new walls for treatment rooms",
                TaskStatus.COMPLETED, today.minusWeeks(3), office));
        taskRepository.save(new Task(null, "Plumbing inspection",
                "Waiting on city inspector availability",
                TaskStatus.BLOCKED, today.plusDays(5), office));
        taskRepository.save(new Task(null, "Final walkthrough",
                "Walkthrough with property manager",
                TaskStatus.COMPLETED, today.minusMonths(3), roof));

        log.info("Loaded demo data: 3 customers, 4 projects, 7 tasks");
    }
}
