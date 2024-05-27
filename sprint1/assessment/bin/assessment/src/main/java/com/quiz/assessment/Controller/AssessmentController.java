package com.quiz.assessment.Controller;

import com.quiz.assessment.EceptionHandling.AssessmentNotFoundException;
import com.quiz.assessment.entity.Assessment;
import com.quiz.assessment.service.AssessmentServiceimp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = {"http://localhost:5173","http://localhost:5174","http://localhost:5175","http://localhost:5176"})
@RequestMapping("/assessments")
public class AssessmentController {

    @Autowired
    private AssessmentServiceimp assessmentService;

    //Create a new Assesssment
    @PostMapping
    public Assessment createNewAssessment(@RequestBody Assessment assessment){
        return assessmentService.createNewAssessment(assessment);
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

    @GetMapping("/{assessmentName}")
    public Optional<Assessment> GetAssessmentByName(@PathVariable String assessmentName) throws AssessmentNotFoundException {
        return assessmentService.GetAssessmentByNmae(assessmentName);
    }

//    @GetMapping("/{id}")
//    public Optional<Assessment> GetAssessmentById(@PathVariable Long id) throws AssementNotFoundException {
//        return assessmentService.GetAssessmentById(id);
//    }


//    Updating Assessment Details
    @PutMapping("/{Id}")
    public Assessment updateAssessment(@PathVariable Long Id, @RequestBody Assessment assessment) throws AssessmentNotFoundException {
        return assessmentService.updateAssessment(Id,assessment);
    }

    @GetMapping("id/{Id}")
    public Optional<Assessment> getAssessmentById(@PathVariable Long Id) throws AssessmentNotFoundException {
        return assessmentService.getAssessmentById(Id);
    }


    @DeleteMapping("/delete/{Id}")

    public String deleteAssessment(@PathVariable Long Id) throws AssessmentNotFoundException {
        assessmentService.deleteassessment(Id);
        return "Successfully deleted the Assessment";
    }

    @PatchMapping("/deactivate/{Id}")
    public String deactivateAssessment(@PathVariable Long Id) throws AssessmentNotFoundException {
        assessmentService.deactivateAssessment(Id);
        return "Successfully deactivated the Assessment";
    }







}
