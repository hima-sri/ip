package com.quiz.assessment.EceptionHandling;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AssessmentExceptionHandler {

    @ExceptionHandler(AssessmentNotFoundException.class)
    public ResponseEntity<String> assessmentNotFoundException(AssessmentNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AssessmentAlreadyExistsException.class)
    public ResponseEntity<String> assessmentAlreadyExistsException(AssessmentAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException  ex) {
        String message = "Duplicate entry. This assessment already exists.";
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }
}