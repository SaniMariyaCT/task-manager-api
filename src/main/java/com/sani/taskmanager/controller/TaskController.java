package com.sani.taskmanager.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sani.taskmanager.dto.BulkTaskResponse;
import com.sani.taskmanager.dto.PaginatedResponse;
import com.sani.taskmanager.dto.TaskRequest;
import com.sani.taskmanager.dto.TaskResponse;
import com.sani.taskmanager.exception.BadRequestException;
import com.sani.taskmanager.service.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping
    public TaskResponse createTask(@Valid @RequestBody TaskRequest request) {
        logger.info("POST /tasks called");
        return service.createTask(request);
    }

    @GetMapping
    public PaginatedResponse<TaskResponse> getTasks(
        @RequestParam(defaultValue = "0") int page, 
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "asc") String direction,
        @RequestParam(required = false) String priority,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) LocalDate dueBefore,
        @RequestParam(required = false) LocalDate dueAfter,
        @RequestParam(required = false) String search) {

        if (dueBefore != null && dueAfter != null) {
            if (dueAfter.isAfter(dueBefore)) {
            throw new BadRequestException("dueAfter cannot be after dueBefore");
            }
        }
            return service.getTasks(page, size, sortBy, direction, priority, status, dueBefore, dueAfter,search);
    }


    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest request) {
        return service.updateTask(id, request);
    }

    @PostMapping("/bulk")
    public BulkTaskResponse createAllTasks(@RequestBody List<TaskRequest> requests) {
        logger.info("POST /tasks/bulk called");
        return service.createAllTasks(requests);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
    service.deleteTask(id);
    return ResponseEntity.noContent().build();
}

    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        return service.getTaskById(id);
    }

}
