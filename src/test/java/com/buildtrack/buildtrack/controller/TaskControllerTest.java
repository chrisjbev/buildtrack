package com.buildtrack.buildtrack.controller;

import com.buildtrack.buildtrack.model.Task;
import com.buildtrack.buildtrack.model.TaskStatus;
import com.buildtrack.buildtrack.service.TaskService;
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
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    void getAllTasksReturnsTasks() throws Exception {
        Task task = new Task(
                1L,
                "Kitchen Demolition",
                "Remove old cabinets",
                TaskStatus.COMPLETED,
                LocalDate.of(2026, 11, 15),
                null
        );

        when(taskService.getAllTasks()).thenReturn(List.of(task));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Kitchen Demolition"));
    }

    @Test
    void getTaskByIdReturnsTask() throws Exception {
        Task task = new Task(
                1L,
                "Kitchen Demolition",
                "Remove old cabinets",
                TaskStatus.COMPLETED,
                LocalDate.of(2026, 11, 15),
                null
        );

        when(taskService.getTaskById(1L)).thenReturn(task);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Kitchen Demolition"));
    }

    @Test
    void getTaskByIdReturnsNotFound() throws Exception {
        when(taskService.getTaskById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/tasks/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTasksByProjectIdReturnsTasks() throws Exception {
        Task task = new Task(
                1L,
                "Kitchen Demolition",
                "Remove old cabinets",
                TaskStatus.COMPLETED,
                LocalDate.of(2026, 11, 15),
                null
        );

        when(taskService.getTasksByProjectId(4L)).thenReturn(List.of(task));

        mockMvc.perform(get("/api/tasks/project/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Kitchen Demolition"));
    }

    @Test
    void createTaskForProjectReturnsCreatedTask() throws Exception {
        Task task = new Task(
                3L,
                "Cabinet Installation",
                "Install new kitchen cabinets",
                TaskStatus.NOT_STARTED,
                LocalDate.of(2026, 12, 1),
                null
        );

        when(taskService.createTaskForProject(eq(4L), any(Task.class)))
                .thenReturn(task);

        mockMvc.perform(post("/api/tasks/project/4")
                        .contentType("application/json")
                        .content("""
                            {
                                "name": "Cabinet Installation",
                                "description": "Install new kitchen cabinets",
                                "status": "NOT_STARTED",
                                "dueDate": "2026-12-01"
                            }
                            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Cabinet Installation"));
    }

    @Test
    void createTaskForProjectReturnsNotFound() throws Exception {
        when(taskService.createTaskForProject(eq(99L), any(Task.class)))
                .thenReturn(null);

        mockMvc.perform(post("/api/tasks/project/99")
                        .contentType("application/json")
                        .content("""
                            {
                                "name": "Cabinet Installation",
                                "description": "Install new kitchen cabinets",
                                "status": "NOT_STARTED",
                                "dueDate": "2026-12-01"
                            }
                            """))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTaskWithInvalidDataReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/tasks/project/4")
                        .contentType("application/json")
                        .content("""
                            {
                                "name": "",
                                "description": "Install new kitchen cabinets",
                                "status": "NOT_STARTED",
                                "dueDate": "2026-12-01"
                            }
                            """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTaskReturnsUpdatedTask() throws Exception {
        Task task = new Task(
                1L,
                "Kitchen Demolition",
                "Remove old cabinets",
                TaskStatus.COMPLETED,
                LocalDate.of(2026, 11, 15),
                null
        );

        when(taskService.updateTask(eq(1L), any(Task.class)))
                .thenReturn(task);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType("application/json")
                        .content("""
                            {
                                "name": "Kitchen Demolition",
                                "description": "Remove old cabinets",
                                "status": "COMPLETED",
                                "dueDate": "2026-11-15"
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void updateTaskReturnsNotFound() throws Exception {
        when(taskService.updateTask(eq(99L), any(Task.class)))
                .thenReturn(null);

        mockMvc.perform(put("/api/tasks/99")
                        .contentType("application/json")
                        .content("""
                            {
                                "name": "Updated Task",
                                "description": "Updated description",
                                "status": "IN_PROGRESS",
                                "dueDate": "2026-12-15"
                            }
                            """))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTaskReturnsNoContent() throws Exception {
        when(taskService.deleteTask(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTaskReturnsNotFound() throws Exception {
        when(taskService.deleteTask(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/tasks/99"))
                .andExpect(status().isNotFound());
    }
}
