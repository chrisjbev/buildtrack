package com.buildtrack.buildtrack.service;

import com.buildtrack.buildtrack.model.Project;
import com.buildtrack.buildtrack.model.Task;
import com.buildtrack.buildtrack.model.TaskStatus;
import com.buildtrack.buildtrack.repository.ProjectRepository;
import com.buildtrack.buildtrack.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTaskForProjectSavesTask() {

        Project project = new Project();
        project.setName("Kitchen Remodel");

        Task task = new Task(
                null,
                "Kitchen Demolition",
                "Remove old cabinets",
                TaskStatus.NOT_STARTED,
                LocalDate.of(2026, 11, 15),
                null
        );

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Task result = taskService.createTaskForProject(1L, task);

        assertNotNull(result);
        assertEquals("Kitchen Demolition", result.getName());
        assertEquals(project, result.getProject());

        verify(taskRepository).save(task);
    }

    @Test
    void createTaskForProjectReturnsNullWhenProjectDoesNotExist() {

        Task task = new Task(
                null,
                "Kitchen Demolition",
                "Remove old cabinets",
                TaskStatus.NOT_STARTED,
                LocalDate.of(2026, 11, 15),
                null
        );

        when(projectRepository.findById(99L))
                .thenReturn(Optional.empty());

        Task result = taskService.createTaskForProject(99L, task);

        assertNull(result);

        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void getTaskByIdReturnsTask() {

        Task task = new Task(
                1L,
                "Kitchen Demolition",
                "Remove old cabinets",
                TaskStatus.NOT_STARTED,
                LocalDate.of(2026, 11, 15),
                null
        );

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals("Kitchen Demolition", result.getName());
        assertEquals(1L, result.getId());
    }

    @Test
    void getTaskByIdReturnsNullWhenTaskDoesNotExist() {

        when(taskRepository.findById(99L))
                .thenReturn(Optional.empty());

        Task result = taskService.getTaskById(99L);

        assertNull(result);
    }

    @Test
    void getTasksByProjectIdReturnsProjectTasks() {

        Task task1 = new Task(
                1L,
                "Kitchen Demolition",
                "Remove old cabinets",
                TaskStatus.NOT_STARTED,
                LocalDate.of(2026, 11, 15),
                null
        );

        Task task2 = new Task(
                2L,
                "Cabinet Installation",
                "Install new cabinets",
                TaskStatus.NOT_STARTED,
                LocalDate.of(2026, 12, 1),
                null
        );

        when(taskRepository.findByProjectId(4L))
                .thenReturn(List.of(task1, task2));

        List<Task> result = taskService.getTasksByProjectId(4L);

        assertEquals(2, result.size());
        assertEquals("Kitchen Demolition", result.get(0).getName());
        assertEquals("Cabinet Installation", result.get(1).getName());
    }

    @Test
    void updateTaskReturnsUpdatedTask() {

        Task existingTask = new Task(
                1L,
                "Kitchen Demolition",
                "Remove old cabinets",
                TaskStatus.NOT_STARTED,
                LocalDate.of(2026, 11, 15),
                null
        );

        Task updatedTask = new Task(
                null,
                "Kitchen Demolition",
                "Remove old cabinets and flooring",
                TaskStatus.COMPLETED,
                LocalDate.of(2026, 11, 15),
                null
        );

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(existingTask));

        when(taskRepository.save(existingTask))
                .thenReturn(existingTask);

        Task result = taskService.updateTask(1L, updatedTask);

        assertNotNull(result);
        assertEquals("Remove old cabinets and flooring", result.getDescription());
        assertEquals(TaskStatus.COMPLETED, result.getStatus());

        verify(taskRepository).save(existingTask);
    }

    @Test
    void updateTaskReturnsNullWhenTaskDoesNotExist() {

        Task updatedTask = new Task(
                null,
                "Kitchen Demolition",
                "Remove old cabinets",
                TaskStatus.COMPLETED,
                LocalDate.of(2026, 11, 15),
                null
        );

        when(taskRepository.findById(99L))
                .thenReturn(Optional.empty());

        Task result = taskService.updateTask(99L, updatedTask);

        assertNull(result);

        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void deleteTaskReturnsTrueWhenTaskExists() {

        when(taskRepository.existsById(1L)).thenReturn(true);

        boolean result = taskService.deleteTask(1L);

        assertTrue(result);

        verify(taskRepository).deleteById(1L);
    }

    @Test
    void deleteTaskReturnsFalseWhenTaskDoesNotExist() {

        when(taskRepository.existsById(99L)).thenReturn(false);

        boolean result = taskService.deleteTask(99L);

        assertFalse(result);

        verify(taskRepository, never()).deleteById(99L);
    }

}
