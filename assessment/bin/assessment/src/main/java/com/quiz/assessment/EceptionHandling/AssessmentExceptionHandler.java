package com.quiz.assessment.EceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AssessmentExceptionHandler {
    @ExceptionHandler(AssessmentNotFoundException.class)
    public ResponseEntity<String> assessmentNotFounfException(AssessmentNotFoundException ex){
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
