package com.thbs.questionMS.service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.thbs.questionMS.dto.QuestionNumbersDto;
import com.thbs.questionMS.exception.QuestionNotFoundException;
import com.thbs.questionMS.exception.SubjectNotFoundException;
import com.thbs.questionMS.model.Question;
import com.thbs.questionMS.repository.QuestionRepository;

@Service
public class QuestionService {
	 
	    private QuestionRepository questionRepository;
	    private SequenceGeneratorService sequenceGeneratorService;
	    
	    
	    public QuestionService(QuestionRepository questionRepository,
				SequenceGeneratorService sequenceGeneratorService) {
			this.questionRepository = questionRepository;
			this.sequenceGeneratorService = sequenceGeneratorService;
		}
		public List<Question> getall(){
	        return questionRepository.findAll();
	    }
	    public Question getQuestionById(Long id) throws QuestionNotFoundException{
	    	Optional<Question> question =  questionRepository.findById(id);
	    	if (question.isPresent()) {
	    		return question.get();
				
			}
	    	else {
	    		 throw new IllegalArgumentException("Question not found");
	    	}
	    	
	    }

	    public List<Question> getQuestionsBySubject(String subject) throws SubjectNotFoundException{
	        List<Question> questions = questionRepository.findQuestionsBySubjectOrderByUsageCountAsc(subject);
	       if(questions.isEmpty()) {
	            throw new SubjectNotFoundException("Questions with the given subject("+subject +") are not found");

	       }
	        else {
	        	return questions;
	        }
	    }


	    public List<Question> getQuestionsByIds(QuestionNumbersDto questionNumbers) {
	    	return  questionRepository.findAllById(questionNumbers.getQuestionNumbers());
	         

	    }
	    public  Question saveQuestion(Question question){
	        question.setQuestionId(sequenceGeneratorService.generateSequence(Question.SEQUENCE_NAME));
	        return questionRepository.save(question);
	    }

	    public void deleteQuestion(Long id){
	        questionRepository.deleteById(id);
	    }
	    
	    
	    
	    public Question updateQuestion(Long id, Map<String, Object> questionMap) throws QuestionNotFoundException {
	        Optional<Question> optionalQuestion = questionRepository.findById(id);
	        if (optionalQuestion.isPresent()) {
	            Question question = optionalQuestion.get(); // Extract the Question object from Optional
	            questionMap.forEach(((key, value) -> {
	                Field field = ReflectionUtils.findField(Question.class, key);
	                field.setAccessible(true);
	                ReflectionUtils.setField(field, question, value);
	            }));
	            return questionRepository.save(question);
	        }
	        // Handle case when Question with given id is not found
	        throw new QuestionNotFoundException("Question not found with the given Id " +  id); // or throw an exception
	    }
	    
	    
	    
}