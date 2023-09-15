package com.example.subscription.controller;

import com.example.subscription.model.ErrorResponseModel;
import com.example.subscription.model.exceptions.InvalidRequestException;
import com.example.subscription.model.exceptions.NoSuchSubscriptionFoundException;
import com.example.subscription.model.exceptions.UserAlreadyExistsException;
import com.example.subscription.util.ValidationProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.text.SimpleDateFormat;
import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @Value("${spring.application.name}")
    private String applicationName;
    @NonNull
    private final ValidationProperties properties;

    /**
     * Custom exception, returns 404 if the requested subscriber does not exist.
     */
    @ExceptionHandler(NoSuchSubscriptionFoundException.class)
    public ResponseEntity<ErrorResponseModel> handleNoSuchSubscriptionFoundException(
            NoSuchSubscriptionFoundException exception, HandlerMethod handlerMethod, HttpServletRequest request) {
        String timeStamp = new SimpleDateFormat(properties.getExceptionDateFormat()).format(new java.util.Date());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseModel.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                        .messages(List.of((exception.getMessage())))
                        .path(request.getRequestURL().toString())
                        .timestamp(timeStamp)
                        .controllerName(handlerMethod.getMethod().getDeclaringClass().toString())
                        .serviceName(handlerMethod.getMethod().getName())
                        .projectName(applicationName)
                        .build());
    }

    /**
     * Custom exception, returns 400 if POST request body contains invalid values.
     */
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponseModel> handleInvalidRequestException(
            InvalidRequestException exception, HandlerMethod handlerMethod, HttpServletRequest request) {
        String timeStamp = new SimpleDateFormat(properties.getExceptionDateFormat()).format(new java.util.Date());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseModel.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .messages(exception.getErrorsMap())
                        .path(request.getRequestURL().toString())
                        .timestamp(timeStamp)
                        .controllerName(handlerMethod.getMethod().getDeclaringClass().toString())
                        .serviceName(handlerMethod.getMethod().getName())
                        .projectName(applicationName)
                        .build());
    }

    /**
     * Custom exception, returns 409 if the created subscriber already exists.
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseModel> handleUserAlreadyExistsException(
            UserAlreadyExistsException exception, HandlerMethod handlerMethod, HttpServletRequest request) {
        String timeStamp = new SimpleDateFormat(properties.getExceptionDateFormat()).format(new java.util.Date());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorResponseModel.builder()
                        .status(HttpStatus.CONFLICT.value())
                        .error(HttpStatus.CONFLICT.getReasonPhrase())
                        .messages(List.of((exception.getMessage())))
                        .path(request.getRequestURL().toString())
                        .timestamp(timeStamp)
                        .controllerName(handlerMethod.getMethod().getDeclaringClass().toString())
                        .serviceName(handlerMethod.getMethod().getName())
                        .projectName(applicationName)
                        .build());
    }
}
