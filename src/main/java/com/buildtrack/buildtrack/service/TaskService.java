package com.buildtrack.buildtrack.service;

import com.buildtrack.buildtrack.model.Project;
import com.buildtrack.buildtrack.model.Task;
import com.buildtrack.buildtrack.repository.ProjectRepository;
import com.buildtrack.buildtrack.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository,
                       ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public List<Task> getTasksByProjectId(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    public Task createTaskForProject(Long projectId, Task task) {
        Project project = projectRepository.findById(projectId).orElse(null);

        if (project == null) {
            return null;
        }

        task.setProject(project);

        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task updatedTask) {
        Task existingTask = getTaskById(id);

        if (existingTask == null) {
            return null;
        }

        existingTask.setName(updatedTask.getName());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setStatus(updatedTask.getStatus());
        existingTask.setDueDate(updatedTask.getDueDate());

        return taskRepository.save(existingTask);
    }

    public boolean deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            return false;
        }

        taskRepository.deleteById(id);
        return true;
    }
}
