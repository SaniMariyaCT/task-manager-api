package com.sani.taskmanager.dto;

import java.util.List;

public class BulkTaskResponse {
    
    private int successCount;
    private int failureCount;
    private List<TaskResponse> successfulTasks;
    private List<String> errors;

    //getters
    public int getSuccessCount() { return successCount;}
    public int getFailureCount() { return failureCount;}
    public List<TaskResponse> getSuccessfulTasks() { return successfulTasks;}
    public List<String> getErrors() { return errors;}

    //setters
    public void setSuccessCount(int successCount) { this.successCount = successCount; }
    public void setFailureCount(int failureCount) { this.failureCount = failureCount; }
    public void setSuccessfulTasks(List<TaskResponse> successfulTasks) { this.successfulTasks = successfulTasks; }
    public void setErrors(List<String> errors) { this.errors = errors; }

}