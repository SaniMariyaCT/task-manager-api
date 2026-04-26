package com.sani.taskmanager.exception;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sani.taskmanager.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException (MethodArgumentNotValidException ex, HttpServletRequest request){
        logger.error("Unhandled exception occurred at {}: ", request.getRequestURI(), ex);
        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .toList();

        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError("Validation Failed");
        response.setMessage("Invalid input data");
        response.setDetails(errors);
        response.setPath(request.getRequestURI());
        return response;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception ex, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setError("Internal Server Error");
        response.setMessage(ex.getMessage());
        response.setPath(request.getRequestURI());
        return response;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(BadRequestException ex, HttpServletRequest request) {
        logger.warn("Bad request: {}", ex.getMessage());
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError("Bad Request");
        response.setMessage(ex.getMessage());
        response.setPath(request.getRequestURI());
        return response;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundRequest(NotFoundException ex, HttpServletRequest request) {
        logger.warn("Resource not found: {}", ex.getMessage());
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setError("Not Found");
        response.setMessage(ex.getMessage());
        response.setPath(request.getRequestURI());
        return response;

    }
}