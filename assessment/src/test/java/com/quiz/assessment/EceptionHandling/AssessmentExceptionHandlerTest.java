package com.quiz.assessment.EceptionHandling;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AssessmentExceptionHandlerTest {

    @InjectMocks
    private AssessmentExceptionHandler assessmentExceptionHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAssessmentNotFoundException() {
        // Given
        AssessmentNotFoundException exception = new AssessmentNotFoundException("Assessment not found");

        // When
        ResponseEntity<String> response = assessmentExceptionHandler.assessmentNotFoundException(exception);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Assessment not found", response.getBody());
    }

    @Test
    public void testAssessmentAlreadyExistsException() {
        // Given
        AssessmentAlreadyExistsException exception = new AssessmentAlreadyExistsException();

        // When
        ResponseEntity<String> response = assessmentExceptionHandler.assessmentAlreadyExistsException(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertEquals("Assessment already exists", response.getBody());
    }

    @Test
    public void testDataIntegrityViolation() {
        // Given
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Duplicate entry");

        // When
        ResponseEntity<String> response = assessmentExceptionHandler.handleDataIntegrityViolation(exception);

        // Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Duplicate entry. This assessment already exists.", response.getBody());
    }
}
