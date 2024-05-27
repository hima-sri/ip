package com.thbs.questionMS.ServiceTests;

import com.thbs.questionMS.dto.QuestionNumbersDto;
import com.thbs.questionMS.exception.QuestionNotFoundException;
import com.thbs.questionMS.exception.SubjectNotFoundException;
import com.thbs.questionMS.model.Question;
import com.thbs.questionMS.repository.QuestionRepository;
import com.thbs.questionMS.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    //positive test cases for GetQuestionById
    @Test
    public void testGetQuestionById_Positive() throws QuestionNotFoundException {
        // Mocking behavior of questionRepository
        Question mockQuestion = new Question();
        mockQuestion.setQuestionId(1L);
        mockQuestion.setTitle("What is the capital of France?");
        when(questionRepository.findById(1L)).thenReturn(Optional.of(mockQuestion));

        // Invoking the method under test
        Question foundQuestion = questionService.getQuestionById(1L);

        // Validating the result
        assertNotNull(foundQuestion);
        assertEquals(1L, foundQuestion.getQuestionId());
        assertEquals("What is the capital of France?", foundQuestion.getTitle());
    }

    //Negative test cases for GetQuestionById

    @Test
    public void testGetQuestionById_Negative_QuestionNotFound() {
        // Mocking behavior of questionRepository
        when(questionRepository.findById(999L)).thenReturn(Optional.empty());

        // Invoking the method under test with a non-existent question ID and expecting an exception
        assertThrows(IllegalArgumentException.class, () -> {
            questionService.getQuestionById(999L);
        });
    }


    @Test
    public void testGetQuestionById_Negative_NullId() {
        // Invoking the method under test with a null question ID and expecting an exception
        assertThrows(IllegalArgumentException.class, () -> {
            questionService.getQuestionById(null);
        });
    }

    // positive case for GetQuestionsBySubject
    @Test
    public void testGetQuestionsBySubject_Positive() throws SubjectNotFoundException {
        // Mocking behavior of questionRepository
        List<Question> mockQuestions = new ArrayList<>();
        Question question1 = new Question();
        question1.setQuestionId(1L);
        question1.setSubject("Mathematics");
        Question question2 = new Question();
        question2.setQuestionId(2L);
        question2.setSubject("Mathematics");
        mockQuestions.add(question1);
        mockQuestions.add(question2);
        when(questionRepository.findQuestionsBySubjectOrderByUsageCountAsc("Mathematics")).thenReturn(mockQuestions);

        // Invoking the method under test
        List<Question> foundQuestions = questionService.getQuestionsBySubject("Mathematics");

        // Validating the result
        assertNotNull(foundQuestions);
        assertEquals(2, foundQuestions.size());
        assertEquals("Mathematics", foundQuestions.get(0).getSubject());
        assertEquals("Mathematics", foundQuestions.get(1).getSubject());
    }

    // Negative case for GetQuestionsBySubject

    @Test
    public void testGetQuestionsBySubject_Negative_EmptyResult() {
        // Mocking behavior of questionRepository to return an empty list for an existing subject
        when(questionRepository.findQuestionsBySubjectOrderByUsageCountAsc("EmptySubject")).thenReturn(Collections.emptyList());

        // Invoking the method under test with an existing subject that has no associated questions and expecting an exception
        assertThrows(SubjectNotFoundException.class, () -> {
            questionService.getQuestionsBySubject("EmptySubject");
        });
    }

    @Test
    public void testGetQuestionsBySubject_Negative_WhitespaceSubject() {
        // Mocking behavior of questionRepository to return an empty list for the trimmed subject
        when(questionRepository.findQuestionsBySubjectOrderByUsageCountAsc("  SubjectWithWhitespace  ")).thenReturn(Collections.emptyList());

        // Invoking the method under test with a subject containing leading and trailing whitespace and expecting an exception
        assertThrows(SubjectNotFoundException.class, () -> {
            questionService.getQuestionsBySubject("  SubjectWithWhitespace  ");
        });
    }

    // positive case for GetQuestionsByIds
    @Test
    public void testGetQuestionsByIds_Positive() {
        // Mocking behavior of questionRepository
        List<Long> questionNumbers = new ArrayList<>();
        questionNumbers.add(1L);
        questionNumbers.add(2L);
        questionNumbers.add(3L);

        List<Question> mockQuestions = new ArrayList<>();
        Question question1 = new Question();
        question1.setQuestionId(1L);
        question1.setTitle("Question 1");
        Question question2 = new Question();
        question2.setQuestionId(2L);
        question2.setTitle("Question 2");
        Question question3 = new Question();
        question3.setQuestionId(3L);
        question3.setTitle("Question 3");

        mockQuestions.add(question1);
        mockQuestions.add(question2);
        mockQuestions.add(question3);

        Mockito.when(questionRepository.findAllById(questionNumbers)).thenReturn(mockQuestions);

        // Creating a QuestionNumbersDto
        QuestionNumbersDto questionNumbersDto = new QuestionNumbersDto();
        questionNumbersDto.setQuestionNumbers(questionNumbers);

        // Invoking the method under test
        List<Question> foundQuestions = questionService.getQuestionsByIds(questionNumbersDto);

        // Validating the result
        assertNotNull(foundQuestions);
        assertEquals(3, foundQuestions.size());
        assertEquals("Question 1", foundQuestions.get(0).getTitle());
        assertEquals("Question 2", foundQuestions.get(1).getTitle());
        assertEquals("Question 3", foundQuestions.get(2).getTitle());
    }

    // Negative case for GetQuestionsByIds
    @Test
    public void testGetQuestionsByIds_EmptyQuestionNumbers() {
        // Creating an empty list of question numbers
        List<Long> emptyQuestionNumbers = Collections.emptyList();

        // Creating a QuestionNumbersDto with empty question numbers list
        QuestionNumbersDto questionNumbersDto = new QuestionNumbersDto();
        questionNumbersDto.setQuestionNumbers(emptyQuestionNumbers);

        // Invoking the method under test
        List<Question> foundQuestions = questionService.getQuestionsByIds(questionNumbersDto);

        // Validating the result
        assertNotNull(foundQuestions);
        assertTrue(foundQuestions.isEmpty());
    }



    @Test
    public void testGetQuestionsByIds_LargeList() {
        // Creating a large list of question numbers
        List<Long> largeQuestionNumbers = new ArrayList<>();
        for (long i = 1L; i <= 10000; i++) {
            largeQuestionNumbers.add(i);
        }

        // Mocking behavior of questionRepository to return an empty list for any question number
        Mockito.when(questionRepository.findAllById(anyList())).thenReturn(Collections.emptyList());

        // Creating a QuestionNumbersDto with large question numbers list
        QuestionNumbersDto questionNumbersDto = new QuestionNumbersDto();
        questionNumbersDto.setQuestionNumbers(largeQuestionNumbers);

        // Invoking the method under test
        List<Question> foundQuestions = questionService.getQuestionsByIds(questionNumbersDto);

        // Validating the result
        assertNotNull(foundQuestions);
        assertTrue(foundQuestions.isEmpty());
    }
}
