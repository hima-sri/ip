package com.thbs.questionMS.controller;

import com.thbs.questionMS.dto.QuestionPaperDTO;
import com.thbs.questionMS.exception.QuestionNotFoundException;
import com.thbs.questionMS.exception.QuestionPaperNotFoundException;
import com.thbs.questionMS.exception.UnableToGenerateQuestionPaperException;
import com.thbs.questionMS.model.Question;
import com.thbs.questionMS.model.QuestionPaper;
import com.thbs.questionMS.service.QuestionPaperService;
import com.thbs.questionMS.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/questionPaper")
public class QuestionPaperController {

    private final QuestionPaperService questionPaperService;
    private final QuestionService questionService;

    @Autowired
    public QuestionPaperController(QuestionPaperService questionPaperService,QuestionService questionService) {
        this.questionPaperService = questionPaperService;
        this.questionService=questionService;
    }


// endpoint to generate QP with specifications
@PostMapping("/generateQP")
public ResponseEntity<?> processQuestionPaper(@RequestBody(required = false) QuestionPaperDTO request) {
    if (request == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request cannot be null");
    }
    if (request.getSubject() == null || request.getSubject().isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Subject cannot be empty");
    }
    if (request.getSubTopic() == null || request.getSubTopic().isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Subtopics cannot be empty");
    }
    if (request.getLevels() == null || request.getLevels().isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Levels cannot be empty");
    }
    try {
        List<Map<String, Object>> response = questionPaperService.processQuestionPaper(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    } catch (UnableToGenerateQuestionPaperException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}

//endpoint to fetch questionIds of the questionPaper using AssessmentId

    @GetMapping("/questionIds/{assessmentId}")
    public ResponseEntity<?> getQuestionIdsByAssessmentId(@PathVariable Long assessmentId) {
        if (assessmentId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid assessment ID");
        }
        try {
            // Fetch the QuestionPaper by Assessment ID
            QuestionPaper questionPaper = questionPaperService.getQuestionPaperByAssessmentId(assessmentId);

            // Retrieve the question IDs and questionPaperId from the QuestionPaper
            List<Long> questionIds = questionPaper.getQuestionIds();
            Long questionPaperId = questionPaper.getQuestionPaperId();

            // Construct the response
            Map<String, Object> response = new HashMap<>();
            response.put("questionPaperId", questionPaperId);
            response.put("questionNumbers", questionIds);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (QuestionPaperNotFoundException ex) {
            // Handle the exception
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question Paper with assessment ID " + assessmentId + " not found");
        }
    }


    //endpoint to get counts of questions that are splitted among the subtopics specified
    @GetMapping("/counts/{assessmentId}")
    public ResponseEntity<?> getQuestionSplitCountAnddifficultyLevelSplitByAssessmentId(@PathVariable Long assessmentId) {
        try {
            // Fetch the QuestionPaper by ID
            QuestionPaper questionPaper = questionPaperService.getQuestionPaperByAssessmentId(assessmentId);

            // Retrieve the question split count from the QuestionPaper
            Map<String, Map<String, Integer>> questionSplitCount = questionPaper.getQuestionsSplitCount();

            // Retrieve the difficulty level split from the QuestionPaper
            Map<String, Integer> difficultyLevelSplit = questionPaper.getDifficultyLevelSplit();

            // Create a custom response object
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("questionSplitCount", questionSplitCount);
            response.put("difficultyLevelSplit", difficultyLevelSplit);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (QuestionPaperNotFoundException e) {
            // Handle QuestionPaperNotFoundException
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question Paper with " + assessmentId + " not found");
        }
    }








    @GetMapping("/assessmentID/{id}")
    public ResponseEntity<Map<String, Object>> getQuestionPaperDetailsByAssessmentId(@PathVariable Long id) throws QuestionPaperNotFoundException, QuestionNotFoundException {
        // Fetch the QuestionPaper by Assessment ID
        QuestionPaper questionPaper = questionPaperService.getQuestionPaperByAssessmentId(id);

        if (questionPaper == null) {
            throw new QuestionPaperNotFoundException("Question paper not found with Assessment ID: " + id);
        }

        // Retrieve the question IDs from the QuestionPaper
        List<Long> questionIds = questionPaper.getQuestionIds();

        // Fetch the questions corresponding to the question IDs
        List<Question> questions = new ArrayList<>();
        for (Long questionId : questionIds) {
            Question question = questionService.getQuestionById(questionId);
            if (question != null) {
                questions.add(question);
            }
        }

        // Prepare the response with question titles, options, and answers
        List<Map<String, Object>> questionDetails = new ArrayList<>();
        for (Question question : questions) {
            Map<String, Object> questionDetail = new LinkedHashMap<>();
            questionDetail.put("title", question.getTitle());
            questionDetail.put("options", question.getOptions());
            questionDetail.put("answers", question.getAnswers());
            questionDetails.add(questionDetail);
        }

        // Prepare the final response
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("questions", questionDetails);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //endpoint to delete the questionPaper by assessmentId
    @DeleteMapping("/deleteAssessment/{assessmentId}")
    public ResponseEntity<?> deleteQuestionPaperByAssessmentId(@PathVariable Long assessmentId) {
        try {
            questionPaperService.deleteQuestionPaperByAssessmentId(assessmentId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (QuestionPaperNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (QuestionNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            // General exception handling, if needed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the question paper.");
        }

    }

}
