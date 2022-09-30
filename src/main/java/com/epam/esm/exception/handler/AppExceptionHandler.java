package com.epam.esm.exception.handler;

import com.epam.esm.dto.response.AppErrorResponseDto;
import com.epam.esm.exception.DataPersistenceException;
import com.epam.esm.exception.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<AppErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new AppErrorResponseDto(HttpStatus.NO_CONTENT.value(), 1801, ex.getMessage()));
    }

    @ExceptionHandler(DataPersistenceException.class)
    public ResponseEntity<AppErrorResponseDto> handleDataPersistenceException(DataPersistenceException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new AppErrorResponseDto(HttpStatus.BAD_REQUEST.value(), 1802, ex.getMessage()));
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new AppErrorResponseDto(status.value(), 1803,
                        Objects.requireNonNull(ex.getFieldError()).getDefaultMessage()));
    }
}
