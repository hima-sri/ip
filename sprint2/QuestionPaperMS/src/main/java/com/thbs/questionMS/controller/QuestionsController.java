package com.thbs.questionMS.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.thbs.questionMS.exception.ExcelProcessingException;
import com.thbs.questionMS.exception.InvalidFileFormatException;
import com.thbs.questionMS.model.Question;
import com.thbs.questionMS.repository.QuestionRepository;
import com.thbs.questionMS.service.QuestionsService;

@RestController
@RequestMapping("/questions")
// @CrossOrigin(origins = "http://localhost:3000")
public class QuestionsController {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionsService questionsService;

    @GetMapping
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file)
            throws ExcelProcessingException, InvalidFileFormatException {
        try {
            questionsService.checkExcelFormat(file);
            List<Question> questions = questionsService.convertExcelToListOfQuestion(file.getInputStream());

            return ResponseEntity.ok(Map.of("message", "File uploaded successfully"));
        } catch (IOException e) {
            // Catch any other unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }

    }
    @GetMapping("/subtopics/{subject}")
    public ResponseEntity<Set<String>> getSubtopicsBySubject(@PathVariable String subject) {
        // Retrieve questions by subject
        List<Question> questions = questionRepository.getQuestionsBySubject(subject);

        // Extract unique subtopics from the questions
        Set<String> subtopics = questions.stream()
                .map(Question::getSubTopic)
                .collect(Collectors.toSet());

        return ResponseEntity.status(HttpStatus.OK).body(subtopics);
    }
//@GetMapping("/subtopics/{subject}")
//public ResponseEntity<Set<String>> getSubtopicsBySubject(@PathVariable String subject) {
//    try {
//        // Retrieve questions by subject
//        List<Question> questions = questionRepository.getQuestionsBySubject(subject);
//
//        // Extract unique subtopics from the questions
//        Set<String> subtopics = questions.stream()
//                .map(Question::getSubTopic)
//                .collect(Collectors.toSet());
//
//        if (subtopics.isEmpty()) {
//            throw new SubtopicsNotFoundException("Subtopics related to subject " + subject + " do not exist.");
//        }
//
//        return ResponseEntity.status(HttpStatus.OK).body(subtopics);
//    } catch (SubjectNotFoundException ex) {
//        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found", ex);
//    }
//}






}
