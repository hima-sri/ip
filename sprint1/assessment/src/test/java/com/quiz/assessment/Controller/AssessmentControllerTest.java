package com.quiz.assessment.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.quiz.assessment.EceptionHandling.AssessmentAlreadyExistsException;
import com.quiz.assessment.EceptionHandling.AssessmentNotFoundException;
import com.quiz.assessment.entity.Assessment;
import com.quiz.assessment.service.AssessmentServiceimp;

public class AssessmentControllerTest {

    @Mock
    private AssessmentServiceimp assessmentService;

    @InjectMocks
    private AssessmentController assessmentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateNewAssessment_Success() {
        // Given
        Assessment assessment = new Assessment();
        when(assessmentService.createNewAssessment(any())).thenReturn(assessment);

        // When
        ResponseEntity<String> responseEntity = assessmentController.createNewAssessment(assessment);

        // Then
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Assessment created successfully", responseEntity.getBody());
    }

    @Test
    public void testCreateNewAssessment_Conflict() {
        // Given
        Assessment assessment = new Assessment();
        when(assessmentService.createNewAssessment(any())).thenThrow(new AssessmentAlreadyExistsException());

        // When
        ResponseEntity<String> responseEntity = assessmentController.createNewAssessment(assessment);

        // Then
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("Assessment already exists", responseEntity.getBody());
    }

    @Test
    public void testGetAllAssessments() {
        // Given
        List<Assessment> assessments = new ArrayList<>();
        when(assessmentService.getallAsessments()).thenReturn(assessments);

        // When
        List<Assessment> result = assessmentController.getAllAssessments();

        // Then
        assertEquals(assessments, result);
    }

    @Test
    public void testGetAllActiveAssessments() {
        // Given
        List<Assessment> activeAssessments = new ArrayList<>();
        when(assessmentService.getActiveAndUpcomingAssessments()).thenReturn(activeAssessments);

        // When
        List<Assessment> result = assessmentController.getAllActiveAssessments();

        // Then
        assertEquals(activeAssessments, result);
    }

    @Test
    public void testGetAllCompletedAssessments() {
        // Given
        List<Assessment> completedAssessments = new ArrayList<>();
        when(assessmentService.getAllCompletedAssessmensts()).thenReturn(completedAssessments);

        // When
        List<Assessment> result = assessmentController.getAllCompletedAssessmensts();

        // Then
        assertEquals(completedAssessments, result);
    }

    @Test
    public void testGetAssessmentByName() throws AssessmentNotFoundException {
        // Given
        String assessmentName = "TestAssessment";
        Assessment assessment = new Assessment();
        when(assessmentService.GetAssessmentByNmae(assessmentName)).thenReturn(Optional.of(assessment));

        // When
        Optional<Assessment> result = assessmentController.GetAssessmentByName(assessmentName);

        // Then
        assertTrue(result.isPresent());
        assertEquals(assessment, result.get());
    }
    

    @Test
    public void testUpdateAssessment() throws AssessmentNotFoundException {
        // Given
        Long assessmentId = 1L;
        Assessment assessment = new Assessment();
        when(assessmentService.updateAssessment(assessmentId, assessment)).thenReturn(assessment);

        // When
        Assessment result = assessmentController.updateAssessment(assessmentId, assessment);

        // Then
        assertEquals(assessment, result);
    }

    @Test
    public void testGetAssessmentById() throws AssessmentNotFoundException {
        // Given
        Long assessmentId = 1L;
        Assessment assessment = new Assessment();
        when(assessmentService.getAssessmentById(assessmentId)).thenReturn(Optional.of(assessment));

        // When
        Optional<Assessment> result = assessmentController.getAssessmentById(assessmentId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(assessment, result.get());
    }

    @Test
    public void testDeleteAssessment() throws AssessmentNotFoundException {
        // Given
        Long assessmentId = 1L;

        // When
        String result = assessmentController.deleteAssessment(assessmentId);

        // Then
        assertEquals("Successfully deleted the Assessment", result);
        verify(assessmentService).deleteassessment(assessmentId);
    }
    
    @Test
    public void testDeactivateAssessment() throws AssessmentNotFoundException {
        // Given
        Long assessmentId = 1L;

        // When
        String result = assessmentController.deactivateAssessment(assessmentId);

        // Then
        assertEquals("Successfully deactivated the Assessment", result);
        verify(assessmentService).deactivateAssessment(assessmentId);
    }

    @Test
    public void testGetTechnologyByBatch() throws AssessmentNotFoundException {
        // Given
        String targetBatch = "Batch1";
        List<String> technologies = new ArrayList<>();
        technologies.add("Technology1");
        when(assessmentService.getTechnologyByBatch(targetBatch)).thenReturn(technologies);

        // When
        List<String> result = assessmentController.getTechnologyByBatch(targetBatch);

        // Then
        assertEquals(technologies, result);
    }
}

