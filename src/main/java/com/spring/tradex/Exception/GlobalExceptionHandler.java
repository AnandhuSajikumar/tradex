package com.spring.tradex.Exception;

import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError resourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return new ApiError(
                404,
                "NOT_FOUND",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError illegalStateException(IllegalStateException ex, HttpServletRequest request) {
        return new ApiError(
                409,
                "CONFLICT",
                ex.getMessage(),
                request.getRequestURI()
        );

    }

    @ExceptionHandler(OptimisticLockException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleVersion(OptimisticLockException ex, HttpServletRequest request) {
        return new ApiError(
                409,
                "CONCURRENT_UPDATE",
                "Task was modified by another request. Please retry.",
                request.getRequestURI()
        );

    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleVersion(IllegalArgumentException ex, HttpServletRequest request) {
        return new ApiError(
                400,
                "INVALID REQUEST",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationErrors(MethodArgumentNotValidException ex,
                                           HttpServletRequest request) {

        StringBuilder sb = new StringBuilder();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            sb.append(error.getField())
                    .append(": ")
                    .append(error.getDefaultMessage())
                    .append("; ");
        }

        return new ApiError(
                400,
                "VALIDATION_FAILED",
                sb.toString(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError badCredentials(BadCredentialsException ex, HttpServletRequest request) {
        return new ApiError(
                401,
                "AUTHENTICATION_FAILED",
                "Invalid email or password",
                request.getRequestURI()
        );
    }
}
