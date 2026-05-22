package com.taskmanager.exception;

import com.taskmanager.dto.response.AbdrassulayevYerzatErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class AbdrassulayevYerzatGlobalExceptionHandler {

    @ExceptionHandler(AbdrassulayevYerzatResourceNotFoundException.class)
    public ResponseEntity<AbdrassulayevYerzatErrorResponse> handleResourceNotFound(
            AbdrassulayevYerzatResourceNotFoundException ex,
            WebRequest request) {

        log.error("Resource not found: {}", ex.getMessage());

        AbdrassulayevYerzatErrorResponse error = AbdrassulayevYerzatErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .path(getPath(request))
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AbdrassulayevYerzatBadRequestException.class)
    public ResponseEntity<AbdrassulayevYerzatErrorResponse> handleBadRequest(
            AbdrassulayevYerzatBadRequestException ex,
            WebRequest request) {

        log.error("Bad request: {}", ex.getMessage());

        AbdrassulayevYerzatErrorResponse error = AbdrassulayevYerzatErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(ex.getMessage())
                .path(getPath(request))
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AbdrassulayevYerzatUnauthorizedException.class)
    public ResponseEntity<AbdrassulayevYerzatErrorResponse> handleUnauthorized(
            AbdrassulayevYerzatUnauthorizedException ex,
            WebRequest request) {

        log.error("Unauthorized: {}", ex.getMessage());

        AbdrassulayevYerzatErrorResponse error = AbdrassulayevYerzatErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Unauthorized")
                .message(ex.getMessage())
                .path(getPath(request))
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<AbdrassulayevYerzatErrorResponse> handleBadCredentials(
            BadCredentialsException ex,
            WebRequest request) {

        log.error("Bad credentials: {}", ex.getMessage());

        AbdrassulayevYerzatErrorResponse error = AbdrassulayevYerzatErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Unauthorized")
                .message("Invalid username or password")
                .path(getPath(request))
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<AbdrassulayevYerzatErrorResponse> handleAccessDenied(
            AccessDeniedException ex,
            WebRequest request) {

        log.error("Access denied: {}", ex.getMessage());

        AbdrassulayevYerzatErrorResponse error = AbdrassulayevYerzatErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .error("Forbidden")
                .message("You don't have permission to access this resource")
                .path(getPath(request))
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AbdrassulayevYerzatErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        log.error("Validation error: {}", ex.getMessage());

        Map<String, String> validationErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            validationErrors.put(error.getField(), error.getDefaultMessage());
        }

        AbdrassulayevYerzatErrorResponse error = AbdrassulayevYerzatErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message("Invalid input data")
                .path(getPath(request))
                .timestamp(LocalDateTime.now())
                .validationErrors(validationErrors)
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<AbdrassulayevYerzatErrorResponse> handleMaxSizeException(
            MaxUploadSizeExceededException ex,
            WebRequest request) {

        log.error("File size exceeded: {}", ex.getMessage());

        AbdrassulayevYerzatErrorResponse error = AbdrassulayevYerzatErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message("File size exceeds the maximum allowed limit (10MB)")
                .path(getPath(request))
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AbdrassulayevYerzatErrorResponse> handleGenericException(
            Exception ex,
            WebRequest request) {

        log.error("Unexpected error occurred: ", ex);

        AbdrassulayevYerzatErrorResponse error = AbdrassulayevYerzatErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("An unexpected error occurred. Please try again later.")
                .path(getPath(request))
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}