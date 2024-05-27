package com.quiz.assessment.Controller;

import com.quiz.assessment.EceptionHandling.AssessmentAlreadyExistsException;
import com.quiz.assessment.EceptionHandling.AssessmentNotFoundException;
import com.quiz.assessment.entity.Assessment;
import com.quiz.assessment.service.AssessmentServiceimp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/assessments")
public class AssessmentController {

    @Autowired
    private AssessmentServiceimp assessmentService;



    //creating a new Assessment
    @PostMapping()
    public ResponseEntity<String> createNewAssessment(@RequestBody Assessment assessment) {
        try {
            assessmentService.createNewAssessment(assessment);
            return new ResponseEntity<>("Assessment created successfully", HttpStatus.CREATED);
        } catch (AssessmentAlreadyExistsException e) {
            return new ResponseEntity<>("Assessment already exists", HttpStatus.CONFLICT);
        }
    }

    //List of Assessments
    @GetMapping
    public List<Assessment> getAllAssessments(){
        return assessmentService.getallAsessments();
    }

    //List of Assessments that are active
    @GetMapping("/active")
    public List<Assessment> getAllActiveAssessments(){
        return assessmentService.getActiveAndUpcomingAssessments();
    }

    //List of Assessments that are completetd

    @GetMapping("/completed")
    public List<Assessment> getAllCompletedAssessmensts(){
        return assessmentService.getAllCompletedAssessmensts();
    }


    //Get Assessment by Assessment Name
    @GetMapping("/{assessmentName}")
    public Optional<Assessment> GetAssessmentByName(@PathVariable String assessmentName) throws AssessmentNotFoundException {
        return assessmentService.GetAssessmentByNmae(assessmentName);
    }


    //Upadate Assessment
    @PutMapping("/{Id}")
    public Assessment updateAssessment(@PathVariable Long Id, @RequestBody Assessment assessment) throws AssessmentNotFoundException {
        return assessmentService.updateAssessment(Id,assessment);
    }

    //Get Assessment by Assessment ID

    @GetMapping("id/{Id}")
    public Optional<Assessment> getAssessmentById(@PathVariable Long Id) throws AssessmentNotFoundException {
        return assessmentService.getAssessmentById(Id);
    }


    //Delete Assessment by Assessment ID
    @DeleteMapping("/delete/{Id}")

    public String deleteAssessment(@PathVariable Long Id) throws AssessmentNotFoundException {
        assessmentService.deleteassessment(Id);
        return "Successfully deleted the Assessment";
    }

    //Deactivate Assessment
    @PatchMapping("/deactivate/{Id}")
    public String deactivateAssessment(@PathVariable Long Id) throws AssessmentNotFoundException {
        assessmentService.deactivateAssessment(Id);
        return "Successfully deactivated the Assessment";
    }


    // Get Batch wise Assessments Technology

    @GetMapping("/technology/{targetbatch}")
    public List<String> getTechnologyByBatch(@PathVariable String targetbatch) throws AssessmentNotFoundException{
        return assessmentService.getTechnologyByBatch(targetbatch);
    }


}
