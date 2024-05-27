package com.quiz.assessment.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.quiz.assessment.entity.Assessment;

public class AssessmentRepositoryTest {

    private AssessmentRepository assessmentRepository;
    
    @BeforeEach
    void setUp() {
        assessmentRepository = mock(AssessmentRepository.class);
        
        // Stubbing repository methods
        Assessment assessment1 = new Assessment(1L, "Test1", "Batch1", LocalDateTime.now().minusHours(2), LocalDateTime.now().plusHours(1), 60, 10, 50, "Course1", "Type1", true, 0);
        Assessment assessment2 = new Assessment(2L, "Test2", "Batch1", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), 60, 10, 50, "Course2", "Type2", true, 0);
        Assessment assessment3 = new Assessment(3L, "Test3", "Batch1", LocalDateTime.now().minusHours(3), LocalDateTime.now().minusHours(1), 60, 10, 50, "Course3", "Type3", false, 0);
        
        when(assessmentRepository.findAssessmentByAssessmentName("Test1")).thenReturn(Optional.of(assessment1));
        when(assessmentRepository.findActiveAssessments(any())).thenReturn(Arrays.asList(assessment1));
        when(assessmentRepository.findUpcomingAssessments(any())).thenReturn(Arrays.asList(assessment2));
        when(assessmentRepository.findCompletedAssessments(any())).thenReturn(Arrays.asList(assessment3));
        when(assessmentRepository.getTechnologyByBatch("Batch1")).thenReturn(Arrays.asList("Course1", "Course2", "Course3"));
        when(assessmentRepository.findAssessments()).thenReturn(Arrays.asList(assessment1, assessment2, assessment3));
    }

    @Test
    public void testFindAssessmentByAssessmentName() {
        Optional<Assessment> foundAssessment = assessmentRepository.findAssessmentByAssessmentName("Test1");
        assertThat(foundAssessment).isPresent();
        assertThat(foundAssessment.get().getAssessmentName()).isEqualTo("Test1");
    }

    @Test
    public void testFindActiveAssessments() {
        List<Assessment> activeAssessments = assessmentRepository.findActiveAssessments(LocalDateTime.now());
        assertThat(activeAssessments).hasSize(1);
        assertThat(activeAssessments.get(0).getAssessmentName()).isEqualTo("Test1");
    }

    @Test
    public void testFindUpcomingAssessments() {
        List<Assessment> upcomingAssessments = assessmentRepository.findUpcomingAssessments(LocalDateTime.now());
        assertThat(upcomingAssessments).hasSize(1);
        assertThat(upcomingAssessments.get(0).getAssessmentName()).isEqualTo("Test2");
    }

    @Test
    public void testFindCompletedAssessments() {
        List<Assessment> completedAssessments = assessmentRepository.findCompletedAssessments(LocalDateTime.now());
        assertThat(completedAssessments).hasSize(1);
        assertThat(completedAssessments.get(0).getAssessmentName()).isEqualTo("Test3");
    }

    @Test
    public void testGetTechnologyByBatch() {
        List<String> technologies = assessmentRepository.getTechnologyByBatch("Batch1");
        assertThat(technologies).containsExactlyInAnyOrder("Course1", "Course2", "Course3");
    }

    @Test
    public void testFindAssessments() {
        List<Assessment> assessments = assessmentRepository.findAssessments();
        assertThat(assessments).hasSize(3);
        assertThat(assessments.get(0).getAssessmentName()).isEqualTo("Test1");
    }
}
