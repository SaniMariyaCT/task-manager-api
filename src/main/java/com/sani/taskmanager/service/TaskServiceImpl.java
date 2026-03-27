package com.sani.taskmanager.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sani.taskmanager.dto.BulkTaskResponse;
import com.sani.taskmanager.dto.PaginatedResponse;
import com.sani.taskmanager.dto.TaskRequest;
import com.sani.taskmanager.dto.TaskResponse;
import com.sani.taskmanager.exception.BadRequestException;
import com.sani.taskmanager.exception.NotFoundException;
import com.sani.taskmanager.mapper.TaskMapper;
import com.sani.taskmanager.model.Task;
import com.sani.taskmanager.model.TaskPriority;
import com.sani.taskmanager.model.TaskStatus;
import com.sani.taskmanager.repository.TaskRepository;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;

    public TaskServiceImpl(TaskRepository repository) {
        this.repository = repository;
    }
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Override
    public TaskResponse createTask(TaskRequest request) {
        logger.info("Create Task API called");
        logger.info("Request received: {}", request);
        Task task = TaskMapper.toEntity(request);
        Task saved = repository.save(task);
        logger.info("Task saved with ID: {}", saved.getId());
        return TaskMapper.toResponse(saved);
    }

    public List<TaskResponse> getAllTasks() {
        List<Task> tasks = repository.findAll();
        List<TaskResponse> responses = new ArrayList<>();
        for(Task task : tasks) {            
            responses.add(TaskMapper.toResponse(task));
        }
        return responses;
    }

    @Override
    public PaginatedResponse<TaskResponse> getTasks(int page, int size, String sortBy, String direction, String priority, String status) {

    logger.info("Fetching tasks | page: {} size: {} sortBy: {} direction: {}", page, size, sortBy, direction);
    Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    Pageable pageable = PageRequest.of(page, size, sort);
    Page<Task> taskPage;
    if (priority != null && status != null) {
        taskPage = repository.findByPriorityAndStatus(TaskPriority.valueOf(priority.toUpperCase()),
                TaskStatus.valueOf(status.toUpperCase()), pageable);
    } else if (priority != null) {
        taskPage = repository.findByPriority(TaskPriority.valueOf(priority.toUpperCase()), pageable);

    } else if (status != null) {
        taskPage = repository.findByStatus(TaskStatus.valueOf(status.toUpperCase()), pageable);

    } else {
        taskPage = repository.findAll(pageable);
    }

    List<TaskResponse> responses = taskPage.getContent().stream().map(TaskMapper::toResponse).toList();    

    PaginatedResponse<TaskResponse> result = new PaginatedResponse<>();
    result.setData(responses);
    result.setPage(taskPage.getNumber());
    result.setSize(taskPage.getSize());
    result.setTotalElements(taskPage.getTotalElements());
    result.setTotalPages(taskPage.getTotalPages());

    logger.info("Returning {} tasks", responses.size());
    return result;
    }

    public TaskResponse updateTask(Long id, TaskRequest request) {

        logger.info("Update Task API called for ID : {}", id);
        logger.info("Request Recieved : {}", request);

        //Find existing task
        Task task = repository.findById(id).orElseThrow(() -> new NotFoundException("Task Not Found with ID : " + id));

        //Update fields
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        try {
            task.setPriority(TaskPriority.valueOf(request.getPriority().toUpperCase()));
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid priority value. Allowed: LOW, MEDIUM, HIGH");
        }
        task.setDueDate(request.getDueDate());
        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.OPEN);
        }

        //save updated fields
        Task updated = repository.save(task);

        //update to response
        return TaskMapper.toResponse(updated);
    }

    public BulkTaskResponse createAllTasks(List<TaskRequest> requests) {
        logger.info("Create Bulk Tasks API called");
        logger.info("Received {} tasks", requests.size());
        // List<Task> tasks = requests.stream().map(TaskMapper::toEntity).toList();
        // List<Task> savedTasks = repository.saveAll(tasks);
        // return savedTasks.stream().map(TaskMapper::toResponse).toList();

        List<TaskResponse> successList = new ArrayList<>();
        List<String> errorList = new ArrayList<>();

        for (int i = 0; i < requests.size(); i++) {
            TaskRequest request = requests.get(i);
            try {
                Task task = TaskMapper.toEntity(request);
                Task saved = repository.save(task);
                successList.add(TaskMapper.toResponse(saved));
            } catch(Exception ex) {
                errorList.add("Task '" + request.getTitle() + "' failed: " + ex.getMessage());
                logger.warn("Failed to process task at index {}: {}", i, ex.getMessage());
            }
        }

        BulkTaskResponse response = new BulkTaskResponse();
        response.setSuccessCount(successList.size());
        response.setFailureCount(errorList.size());
        response.setSuccessfulTasks(successList);
        response.setErrors(errorList);
        return response;
    }

    public void deleteTask(Long id) {
        logger.info("Delete Task API called for ID: {}", id);
        Task task = repository.findById(id).orElseThrow(() -> new NotFoundException("Task Not Found with ID : " + id) );
        repository.delete(task);
        logger.info("Task deleted with ID: {}", id);

    }

    public TaskResponse getTaskById(Long id) {
        logger.info("Get Task by ID called: {}", id);
        Task task = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Task not found with ID: " + id));
        return TaskMapper.toResponse(task);
    }
}