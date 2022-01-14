package io.codero.filestore.controller;

import io.codero.filestore.exception.FileAlreadyExistException;
import io.codero.filestore.exception.FileNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@RestControllerAdvice
public class FileStoreExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> handleFileNotFoundException(FileNotFoundException exception) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(FileAlreadyExistException.class)
    public ResponseEntity<?> handleFileAlreadyExistException(FileAlreadyExistException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIOException(IOException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }
}
