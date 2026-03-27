package com.sani.taskmanager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sani.taskmanager.model.Task;
import com.sani.taskmanager.model.TaskPriority;
import com.sani.taskmanager.model.TaskStatus;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByPriority (TaskPriority priority, Pageable pageable);
    Page<Task> findByStatus (TaskStatus status, Pageable pageable);
    Page<Task> findByPriorityAndStatus (TaskPriority priority, TaskStatus status, Pageable pageable);

}