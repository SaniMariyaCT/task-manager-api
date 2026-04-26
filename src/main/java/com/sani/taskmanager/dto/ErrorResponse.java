package com.sani.taskmanager.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private List<String> details;
    private String path;

    // getters & setters
    public LocalDateTime getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getMessage() { return message; }
    public List<String> getDetails() { return details; }
    public String getPath() { return path; }

    public void setTimestamp( LocalDateTime timestamp) { this.timestamp = timestamp; } 
    public void setStatus( int status) { this.status = status; } 
    public void setError( String error) { this.error = error; } 
    public void setMessage( String message) { this.message = message; } 
    public void setDetails( List<String> details) { this.details = details; } 
    public void setPath( String path) { this.path = path; } 

}