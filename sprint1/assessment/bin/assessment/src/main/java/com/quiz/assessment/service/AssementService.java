package com.quiz.assessment.service;

import com.quiz.assessment.EceptionHandling.AssessmentNotFoundException;
import com.quiz.assessment.entity.Assessment;

import java.util.List;
import java.util.Optional;

public interface AssementService {

    public Assessment createNewAssessment(Assessment assessment);

    List<Assessment> getallAsessments();

    Optional<Assessment> GetAssessmentByNmae(String assessmentName) throws AssessmentNotFoundException;

    Assessment updateAssessment(Long id, Assessment assessment) throws AssessmentNotFoundException;

    void deleteassessment(Long id) throws AssessmentNotFoundException;

    List<Assessment> getActiveAndUpcomingAssessments();

    List<Assessment> getAllCompletedAssessmensts();

    void deactivateAssessment(Long id) throws AssessmentNotFoundException;

   Optional<Assessment> getAssessmentById(Long id) throws AssessmentNotFoundException;


}
