package com.rhythmo.rhythmobackend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NoSuchEntityException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleNoSuchEntity(NoSuchEntityException e, HttpServletRequest request) {
        return buildProblemDetail(request, HttpStatus.NOT_FOUND, e.getMessage(), "Entity not found");
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ProblemDetail handleAuthenticationErrorResponseException(AuthenticationException e, HttpServletRequest request) {
        return buildProblemDetail(request, HttpStatus.UNAUTHORIZED, e.getMessage(), "Authentication issue");
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail handleRuntime(RuntimeException e, HttpServletRequest request) {
        return buildProblemDetail(request, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "Internal error");
    }

    private ProblemDetail buildProblemDetail(HttpServletRequest request, HttpStatus status, String msg, String title) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, msg);
        problemDetail.setTitle(title);
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }
}
