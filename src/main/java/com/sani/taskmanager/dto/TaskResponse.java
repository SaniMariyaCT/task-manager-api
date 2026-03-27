package com.sani.taskmanager.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDate dueDate;

    // public String getTitle() { return title; }
    // public void setTitle(String title) { this.title = title; }
    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

}