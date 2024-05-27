package com.quiz.assessment.service;

import com.quiz.assessment.EceptionHandling.AssessmentNotFoundException;
import com.quiz.assessment.entity.Assessment;
import com.quiz.assessment.repository.AssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class AssessmentServiceimp implements AssementService{

    @Autowired
    private AssessmentRepository assessmentRepository;

    //creating a new assessment
    @Override
    public Assessment createNewAssessment(Assessment assessment) {
        return assessmentRepository.save(assessment);
    }

    //get All Assessmets
    @Override
    public List<Assessment> getallAsessments() {
        return assessmentRepository.findAssessments();
    }

    //Get Assessment by assessment name
    @Override
    public Optional<Assessment> GetAssessmentByNmae(String assessmentName) throws AssessmentNotFoundException {

        Optional<Assessment> tmpAssessment =assessmentRepository.findAssessmentByAssessmentName(assessmentName);
        if(tmpAssessment.isPresent()) {
            return tmpAssessment;
        }
        else {
            throw new AssessmentNotFoundException("Assessment not found with this name: "+assessmentName);
        }
    }

    //Updating Assessment Details

    @Override
    public Assessment updateAssessment(Long id, Assessment tmpAssessment) throws AssessmentNotFoundException {
        Optional<Assessment> optionalAssessment = assessmentRepository.findById(id);
        if (optionalAssessment.isPresent()) {
            Assessment assessment = optionalAssessment.get();
            assessment.setAssessmentName(tmpAssessment.getAssessmentName());
            assessment.setTargetBatch(tmpAssessment.getTargetBatch());
            assessment.setStartTime(tmpAssessment.getStartTime());
            assessment.setEndTime(tmpAssessment.getEndTime());
            assessment.setDurationMinutes(tmpAssessment.getDurationMinutes());
            assessment.setNumberOfQuestions(tmpAssessment.getNumberOfQuestions());
            assessment.setPassingPercentage(tmpAssessment.getPassingPercentage());
            assessment.setNumberOfVoilations(tmpAssessment.getNumberOfVoilations());
            return assessmentRepository.save(assessment);
        } else {
            throw new AssessmentNotFoundException("Assessment not found with this id: " + id);
        }
    }

    //Deleting the Assessment by Id
    @Override
    public void deleteassessment(Long id) throws AssessmentNotFoundException {
        Optional<Assessment> optionalAssessment = assessmentRepository.findById(id);
        if (optionalAssessment.isPresent()) {
            assessmentRepository.deleteById(id);
        }
        else {
            throw new AssessmentNotFoundException("Assessment not found with this id: " + id);
        }
    }


    //List of Assessments that are active and upcoming
    @Override
    public List<Assessment> getActiveAndUpcomingAssessments() {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Assessment> activeAssessments = assessmentRepository.findActiveAssessments(currentTime);
        List<Assessment> upcomingAssessments = assessmentRepository.findUpcomingAssessments(currentTime);
        List<Assessment> allAssessments = new ArrayList<>();
        allAssessments.addAll(activeAssessments);
        allAssessments.addAll(upcomingAssessments);
        allAssessments.sort(Comparator.comparingLong(Assessment::getId).reversed());
        return allAssessments;
    }

    //List of Assessments that are completed

    @Override
    public List<Assessment> getAllCompletedAssessmensts() {
        LocalDateTime currentTime = LocalDateTime.now();
        return assessmentRepository.findCompletedAssessments(currentTime);

    }

    @Override
    public void deactivateAssessment(Long id) throws AssessmentNotFoundException {
        Optional<Assessment> assessment = assessmentRepository.findById(id);
        if (assessment.isPresent()) {
            Assessment tmpAssessment = assessment.get();
            tmpAssessment.setStatus(false);
            assessmentRepository.save(tmpAssessment);
        } else {
            throw new AssessmentNotFoundException("Assessment not found with id: " + id);
        }
    }




    @Override
    public Optional<Assessment> getAssessmentById(Long id) throws AssessmentNotFoundException {
        Optional<Assessment> tmpAssessment =assessmentRepository.findById(id);
        if(tmpAssessment.isPresent()) {
            return tmpAssessment;
        }
        else {
            throw new AssessmentNotFoundException("Assessment not found with this id: "+id);
        }
    }



}
