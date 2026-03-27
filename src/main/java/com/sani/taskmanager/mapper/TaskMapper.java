package com.sani.taskmanager.mapper;

import com.sani.taskmanager.dto.TaskRequest;
import com.sani.taskmanager.dto.TaskResponse;
import com.sani.taskmanager.exception.BadRequestException;
import com.sani.taskmanager.model.Task;
import com.sani.taskmanager.model.TaskPriority;
import com.sani.taskmanager.model.TaskStatus;

public class TaskMapper {
    public static Task toEntity(TaskRequest request) {

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());

        try {
            task.setPriority(TaskPriority.valueOf(request.getPriority().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid priority value. Allowed: LOW, MEDIUM, HIGH");
        }

        task.setStatus(TaskStatus.OPEN);
        task.setDueDate(request.getDueDate());
        return task;
    }

    public static TaskResponse toResponse (Task task) {

        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus().name());
        response.setPriority(task.getPriority().name());
        response.setDueDate(task.getDueDate());

        return response;

    }
}