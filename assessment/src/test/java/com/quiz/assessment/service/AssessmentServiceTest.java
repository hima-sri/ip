package com.quiz.assessment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.quiz.assessment.EceptionHandling.AssessmentNotFoundException;
import com.quiz.assessment.entity.Assessment;
import com.quiz.assessment.repository.AssessmentRepository;

public class AssessmentServiceTest {

    @Mock
    private AssessmentRepository assessmentRepository;

    @InjectMocks
    private AssessmentServiceimp assessmentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateNewAssessment() {
        Assessment assessment = new Assessment();
        when(assessmentRepository.save(any(Assessment.class))).thenReturn(assessment);
        Assessment savedAssessment = assessmentService.createNewAssessment(assessment);
        assertEquals(assessment, savedAssessment);
    }

    @Test
    public void testGetAllAssessments() {
        List<Assessment> assessments = new ArrayList<>();
        assessments.add(new Assessment());
        when(assessmentRepository.findAssessments()).thenReturn(assessments);
        List<Assessment> retrievedAssessments = assessmentService.getallAsessments();
        assertEquals(1, retrievedAssessments.size());
    }
    @Test
    public void testGetAssessmentByNmae_AssessmentFound() throws AssessmentNotFoundException {
        // Given
        String assessmentName = "TestAssessment";
        Assessment assessment = new Assessment();
        assessment.setAssessmentName(assessmentName);
        when(assessmentRepository.findAssessmentByAssessmentName(assessmentName)).thenReturn(Optional.of(assessment));

        // When
        Optional<Assessment> retrievedAssessment = assessmentService.GetAssessmentByNmae(assessmentName);

        // Then
        assertTrue(retrievedAssessment.isPresent());
        assertEquals(assessmentName, retrievedAssessment.get().getAssessmentName());
    }

    @Test
    public void testGetAssessmentByNmae_AssessmentNotFound() {
        // Given
        String assessmentName = "NonExistentAssessment";
        when(assessmentRepository.findAssessmentByAssessmentName(assessmentName)).thenReturn(Optional.empty());

        // When & Then
        AssessmentNotFoundException exception = assertThrows(AssessmentNotFoundException.class, () -> {
            assessmentService.GetAssessmentByNmae(assessmentName);
        });

        assertEquals("Assessment not found with this name: " + assessmentName, exception.getMessage());
    }
  

    @Test
    public void testUpdateAssessment_AssessmentFound() throws AssessmentNotFoundException {
        // Given
        Long assessmentId = 1L;
        Assessment existingAssessment = new Assessment();
        existingAssessment.setAssessmentId(assessmentId);
        existingAssessment.setAssessmentName("Existing Assessment");
        existingAssessment.setTargetBatch("Batch1");
        existingAssessment.setStartTime(LocalDateTime.now());
        existingAssessment.setEndTime(LocalDateTime.now().plusHours(1));
        existingAssessment.setDurationMinutes(60);
        existingAssessment.setNumberOfQuestions(10);
        existingAssessment.setCourseName("Course1");
        existingAssessment.setAssessmentType("Type1");
        existingAssessment.setPassingPercentage(50);
        existingAssessment.setNumberOfViolations(0);

        Assessment updatedAssessment = new Assessment();
        updatedAssessment.setAssessmentId(assessmentId);
        updatedAssessment.setAssessmentName("Updated Assessment");
        updatedAssessment.setTargetBatch("Updated Batch");
        updatedAssessment.setStartTime(LocalDateTime.now().plusHours(2));
        updatedAssessment.setEndTime(LocalDateTime.now().plusHours(3));
        updatedAssessment.setDurationMinutes(90);
        updatedAssessment.setNumberOfQuestions(20);
        updatedAssessment.setCourseName("Course2");
        updatedAssessment.setAssessmentType("Type2");
        updatedAssessment.setPassingPercentage(60);
        updatedAssessment.setNumberOfViolations(5);

        Optional<Assessment> optionalAssessment = Optional.of(existingAssessment);
        when(assessmentRepository.findById(assessmentId)).thenReturn(optionalAssessment);
        when(assessmentRepository.save(any())).thenReturn(updatedAssessment);

        // When
        Assessment result = assessmentService.updateAssessment(assessmentId, updatedAssessment);

        // Then
        assertEquals(updatedAssessment.getAssessmentId(), result.getAssessmentId());
        assertEquals(updatedAssessment.getAssessmentName(), result.getAssessmentName());
        assertEquals(updatedAssessment.getTargetBatch(), result.getTargetBatch());
        assertEquals(updatedAssessment.getStartTime(), result.getStartTime());
        assertEquals(updatedAssessment.getEndTime(), result.getEndTime());
        assertEquals(updatedAssessment.getDurationMinutes(), result.getDurationMinutes());
        assertEquals(updatedAssessment.getNumberOfQuestions(), result.getNumberOfQuestions());
        assertEquals(updatedAssessment.getCourseName(), result.getCourseName());
        assertEquals(updatedAssessment.getAssessmentType(), result.getAssessmentType());
        assertEquals(updatedAssessment.getPassingPercentage(), result.getPassingPercentage());
        assertEquals(updatedAssessment.getNumberOfViolations(), result.getNumberOfViolations());
    }
    
    @Test
    public void testUpdateAssessment_AssessmentNotFound() {
        // Given
        Long nonExistentAssessmentId = 2L;
        when(assessmentRepository.findById(nonExistentAssessmentId)).thenReturn(Optional.empty());

        Assessment updatedAssessment = new Assessment();
        updatedAssessment.setAssessmentName("Updated Assessment Name");

        // When & Then
        AssessmentNotFoundException exception = assertThrows(AssessmentNotFoundException.class, () -> {
            assessmentService.updateAssessment(nonExistentAssessmentId, updatedAssessment);
        });

        assertEquals("Assessment not found with this id: " + nonExistentAssessmentId, exception.getMessage());
        verify(assessmentRepository, never()).save(any(Assessment.class));
    }
    
    @Test
    public void testDeleteAssessment_AssessmentFound() throws AssessmentNotFoundException {
        // Given
        Long assessmentId = 1L;
        Assessment existingAssessment = new Assessment();
        existingAssessment.setAssessmentId(assessmentId);
        when(assessmentRepository.findById(assessmentId)).thenReturn(Optional.of(existingAssessment));

        // When
        assessmentService.deleteassessment(assessmentId);

        // Then
        verify(assessmentRepository, times(1)).deleteById(assessmentId);
    }

    @Test
    public void testDeleteAssessment_AssessmentNotFound() {
        // Given
        Long nonExistentAssessmentId = 2L;
        when(assessmentRepository.findById(nonExistentAssessmentId)).thenReturn(Optional.empty());

        // When & Then
        AssessmentNotFoundException exception = assertThrows(AssessmentNotFoundException.class, () -> {
            assessmentService.deleteassessment(nonExistentAssessmentId);
        });

        assertEquals("Assessment not found with this id: " + nonExistentAssessmentId, exception.getMessage());
        verify(assessmentRepository, never()).deleteById(anyLong());
    }
    
    @Test
    public void testGetActiveAndUpcomingAssessments() {
        // Given
        LocalDateTime currentTime = LocalDateTime.now();
        List<Assessment> activeAssessments = new ArrayList<>();
        List<Assessment> upcomingAssessments = new ArrayList<>();
        List<Assessment> combinedList = new ArrayList<>();
        when(assessmentRepository.findActiveAssessments(currentTime)).thenReturn(activeAssessments);
        when(assessmentRepository.findUpcomingAssessments(currentTime)).thenReturn(upcomingAssessments);

        // When
        List<Assessment> result = assessmentService.getActiveAndUpcomingAssessments();

        // Then
        assertEquals(combinedList, result);
    }
    
    @Test
    public void testGetAllCompletedAssessmensts() {
        // Given
        LocalDateTime currentTime = LocalDateTime.now();
        List<Assessment> completedAssessments = new ArrayList<>();
        when(assessmentRepository.findCompletedAssessments(currentTime)).thenReturn(completedAssessments);

        // When
        List<Assessment> result = assessmentService.getAllCompletedAssessmensts();

        // Then
        assertEquals(completedAssessments, result);
    }
    
    @Test
    public void testDeactivateAssessment_AssessmentFound() throws AssessmentNotFoundException {
        // Given
        Long assessmentId = 1L;
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);
        Optional<Assessment> optionalAssessment = Optional.of(assessment);
        when(assessmentRepository.findById(assessmentId)).thenReturn(optionalAssessment);

        // When
        assessmentService.deactivateAssessment(assessmentId);

        // Then
        assertFalse(assessment.getStatus());
        verify(assessmentRepository, times(1)).save(assessment);
    }

    @Test
    public void testDeactivateAssessment_AssessmentNotFound() {
        // Given
        Long nonExistentAssessmentId = 2L;
        when(assessmentRepository.findById(nonExistentAssessmentId)).thenReturn(Optional.empty());

        // When & Then
        AssessmentNotFoundException exception = assertThrows(AssessmentNotFoundException.class, () -> {
            assessmentService.deactivateAssessment(nonExistentAssessmentId);
        });

        assertEquals("Assessment not found with id: " + nonExistentAssessmentId, exception.getMessage());
        verify(assessmentRepository, never()).save(any());
    }
    
    @Test
    public void testGetAssessmentById_AssessmentFound() throws AssessmentNotFoundException {
        // Given
        Long assessmentId = 1L;
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);
        Optional<Assessment> optionalAssessment = Optional.of(assessment);
        when(assessmentRepository.findById(assessmentId)).thenReturn(optionalAssessment);

        // When
        Optional<Assessment> result = assessmentService.getAssessmentById(assessmentId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(assessment, result.get());
    }

    @Test
    public void testGetAssessmentById_AssessmentNotFound() {
        // Given
        Long nonExistentAssessmentId = 2L;
        when(assessmentRepository.findById(nonExistentAssessmentId)).thenReturn(Optional.empty());

        // When & Then
        AssessmentNotFoundException exception = assertThrows(AssessmentNotFoundException.class, () -> {
            assessmentService.getAssessmentById(nonExistentAssessmentId);
        });

        assertEquals("Assessment not found with this id: " + nonExistentAssessmentId, exception.getMessage());
    }
    
    @Test
    public void testGetTechnologyByBatch() {
        // Given
        String targetBatch = "Batch1";
        List<String> technologies = new ArrayList<>();
        technologies.add("Technology1");
        technologies.add("Technology2");
        when(assessmentRepository.getTechnologyByBatch(targetBatch)).thenReturn(technologies);

        // When
        List<String> result = assessmentService.getTechnologyByBatch(targetBatch);

        // Then
        assertEquals(technologies, result);
    }
}
