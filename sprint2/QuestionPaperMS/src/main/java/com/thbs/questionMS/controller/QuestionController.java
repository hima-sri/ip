package com.thbs.questionMS.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.thbs.questionMS.dto.QuestionNumbersDto;
import com.thbs.questionMS.exception.QuestionNotFoundException;
import com.thbs.questionMS.exception.SubjectNotFoundException;
import com.thbs.questionMS.model.Question;
import com.thbs.questionMS.service.QuestionService;

@RestController
@RequestMapping("/questions")
public class QuestionController {

	private QuestionService questionService;


	public QuestionController(QuestionService questionService) {
		super();
		this.questionService = questionService;
	}


	//endpoint for getting all the Questions
	@GetMapping("all") 
	public ResponseEntity<List<Question>> getall() {
		return ResponseEntity.ok(questionService.getall());
	}


	//endpoint for getting Questions by subject
	@GetMapping("{subject}")
	public List<Question> getQuestions(@PathVariable("subject") String subject) throws SubjectNotFoundException {
		return questionService.getQuestionsBySubject(subject);
	}

	//endpoint for getting Questions by questionIds - [List ]
//	@GetMapping("/getQuestions")
//	public ResponseEntity<List<Question>> getQuestions(@RequestBody QuestionNumbersDto questionNumbers) {
//		return ResponseEntity.ok(questionService.getQuestionsByIds(questionNumbers));
//	}

//	@GetMapping("/getQuestions")
//	public ResponseEntity<List<Question>> getQuestionsByIds(@RequestParam("questionNumbers") String questionNumbers) {
//		List<Long> parsedQuestionNumbers = Arrays.stream(questionNumbers.split(","))
//				.map(Long::valueOf)
//				.collect(Collectors.toList());
//
//		// Create a new QuestionNumbersDto object and set the list of question numbers
//		QuestionNumbersDto questionNumbersDto = new QuestionNumbersDto();
//		questionNumbersDto.setQuestionNumbers(parsedQuestionNumbers);
//
//		// Pass the QuestionNumbersDto object to the service method
//		List<Question> questions = questionService.getQuestionsByIds(questionNumbersDto);
//
//		return ResponseEntity.ok(questions);
//	}
//
@GetMapping("/getQuestions")
public ResponseEntity<List<Question>> getQuestionsByIds(@RequestParam("questionNumbers") String questionNumbers) {
	List<Long> parsedQuestionNumbers = Arrays.stream(questionNumbers.split(","))
			.filter(s -> !s.isEmpty()) // Filter out empty strings
			.map(Long::valueOf)
			.collect(Collectors.toList());

	// Create a new QuestionNumbersDto object and set the list of question numbers
	QuestionNumbersDto questionNumbersDto = new QuestionNumbersDto();
	questionNumbersDto.setQuestionNumbers(parsedQuestionNumbers);

	// Pass the QuestionNumbersDto object to the service method
	List<Question> questions = questionService.getQuestionsByIds(questionNumbersDto);

	return ResponseEntity.ok(questions);
}

	//endpoint for posting a Question
	@PostMapping("add") 
	public ResponseEntity<Question> addOne(@RequestBody Question question) {
		return ResponseEntity.ok(questionService.saveQuestion(question));
	}

	//endpoint for deleting a  Question
	@DeleteMapping("delete/{id}")
	public void deleteQuestion(@PathVariable(("id")) Long id) {
		questionService.deleteQuestion(id);
	}

	//endpoint for updating a Question
	@PatchMapping("update/{id}")
	public ResponseEntity<Question> updateQuestion(@PathVariable Long id,@RequestBody Map<String, Object> question) throws QuestionNotFoundException{
		return new ResponseEntity<>(questionService.updateQuestion(id,  question), HttpStatus.CREATED);
	}


}
