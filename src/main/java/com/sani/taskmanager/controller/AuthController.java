package com.sani.taskmanager.controller;

import com.sani.taskmanager.dto.LoginRequest;
import com.sani.taskmanager.dto.LoginResponse;
import com.sani.taskmanager.security.JwtUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) 
    {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public LoginResponse login (@RequestBody LoginRequest request) {
                
        // TEMP: hardcoded user (we'll improve later)
        if ("admin".equals(request.getUsername()) &&
            "password".equals(request.getPassword())) {

            String token = jwtUtil.generateToken(request.getUsername());
            return new LoginResponse(token);
        }

        throw new RuntimeException("Invalid credentials");
    }
}
