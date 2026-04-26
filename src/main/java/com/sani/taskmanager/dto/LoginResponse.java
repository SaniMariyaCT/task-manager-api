package com.sani.taskmanager.dto;

public class LoginResponse {
    
    private String token;

    public  String getToken() { return token; }
    public LoginResponse(String token) { this.token = token;}

}
