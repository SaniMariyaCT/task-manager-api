package com.sani.taskmanager.service;

import java.time.LocalDate;
import java.util.List;

import com.sani.taskmanager.dto.BulkTaskResponse;
import com.sani.taskmanager.dto.PaginatedResponse;
import com.sani.taskmanager.dto.TaskRequest;
import com.sani.taskmanager.dto.TaskResponse;

public interface TaskService {

    TaskResponse createTask(TaskRequest request);
    List<TaskResponse> getAllTasks();
    PaginatedResponse<TaskResponse> getTasks(int page, int size, String sortBy
        , String direction, String priority, String status
        , LocalDate dueBefore, LocalDate dueAfter, String search);
    TaskResponse updateTask(Long id, TaskRequest request);
    BulkTaskResponse createAllTasks(List<TaskRequest> requests);
    void deleteTask(Long id);
    TaskResponse getTaskById(Long id);
}