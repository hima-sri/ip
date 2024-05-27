package com.thbs.questionMS.ControllerTests;

import com.thbs.questionMS.controller.QuestionPaperController;
import com.thbs.questionMS.dto.QuestionPaperDTO;
import com.thbs.questionMS.exception.QuestionNotFoundException;
import com.thbs.questionMS.exception.QuestionPaperNotFoundException;
import com.thbs.questionMS.exception.UnableToGenerateQuestionPaperException;
import com.thbs.questionMS.model.Question;
import com.thbs.questionMS.model.QuestionPaper;
import com.thbs.questionMS.service.QuestionPaperService;
import com.thbs.questionMS.service.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionPaperControllerTests {

    @Mock
    private QuestionPaperService questionPaperService;

    @Mock
    private QuestionService questionService;
    @InjectMocks
    private QuestionPaperController questionPaperController;

    @Mock
    private List<Map<String, Object>> mockResponse;
    @Mock
    private QuestionPaper sampleQuestionPaper;

    @Mock
    private List<Question> sampleQuestions;

    @Mock
    private QuestionPaperDTO validRequest;
    @BeforeEach
    public void setUp() {
        validRequest = new QuestionPaperDTO();
        validRequest.setAssessmentId(1L);
        validRequest.setSubject("Math");
        validRequest.setSubTopic(List.of("Algebra", "Geometry"));
        validRequest.setLevels(Map.of("Easy", 10, "Medium", 20, "Hard", 5));

        mockResponse = new ArrayList<>();
        Map<String, Object> question1 = new HashMap<>();
        question1.put("id", 1);
        question1.put("title", "What is the capital of France?");
        question1.put("options", List.of("Paris", "London", "Berlin", "Madrid"));
        mockResponse.add(question1);
    }


    //positive testcase for processqp


    @Test
    public void testProcessQuestionPaper_Positive() throws UnableToGenerateQuestionPaperException {
        // Mocking the service response
        when(questionPaperService.processQuestionPaper(any(QuestionPaperDTO.class))).thenReturn(mockResponse);

        // Calling the controller method
        ResponseEntity<?> responseEntity = questionPaperController.processQuestionPaper(validRequest);

        // Asserting the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockResponse, responseEntity.getBody());
    }
    //negatuve testcase for generateqp


    @Test
    public void testProcessQuestionPaper_EmptySubject() throws UnableToGenerateQuestionPaperException {
        QuestionPaperDTO request = new QuestionPaperDTO();
        request.setSubject("");
        ResponseEntity<?> responseEntity = questionPaperController.processQuestionPaper(request);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Subject cannot be empty", responseEntity.getBody());
    }




    @Test
    public void testProcessQuestionPaper_NullRequest(){
        ResponseEntity<?> responseEntity = questionPaperController.processQuestionPaper(null);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Request cannot be null", responseEntity.getBody());
    }

    @Test
    public void testProcessQuestionPaper_AssessmentIdExists() throws UnableToGenerateQuestionPaperException {
        QuestionPaperDTO request = new QuestionPaperDTO();
        request.setSubject("Math");
        request.setSubTopic(Arrays.asList("Algebra", "Geometry"));
        request.setLevels(Map.of("Easy", 10, "Medium", 20, "Hard", 5));
        request.setAssessmentId(1L);

        when(questionPaperService.processQuestionPaper(any(QuestionPaperDTO.class)))
                .thenThrow(new UnableToGenerateQuestionPaperException("Assessment ID already exists"));

        ResponseEntity<?> responseEntity = questionPaperController.processQuestionPaper(request);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Assessment ID already exists", responseEntity.getBody());
    }

    @Test
    public void testProcessQuestionPaper_InsufficientQuestions() throws UnableToGenerateQuestionPaperException {
        QuestionPaperDTO request = new QuestionPaperDTO();
        request.setSubject("Math");
        request.setSubTopic(Arrays.asList("Algebra", "Geometry"));
        request.setLevels(Map.of("Easy", 10, "Medium", 20, "Hard", 5));

        when(questionPaperService.processQuestionPaper(any(QuestionPaperDTO.class)))
                .thenThrow(new UnableToGenerateQuestionPaperException("Not enough questions available for Hard level"));

        ResponseEntity<?> responseEntity = questionPaperController.processQuestionPaper(request);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Not enough questions available for Hard level", responseEntity.getBody());
    }

    @Test
    public void testProcessQuestionPaper_EmptyLevels() {
        QuestionPaperDTO request = new QuestionPaperDTO();
        request.setSubject("Math");
        request.setSubTopic(List.of("Algebra", "Geometry"));
        request.setLevels(Collections.emptyMap());

        ResponseEntity<?> responseEntity = questionPaperController.processQuestionPaper(request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Levels cannot be empty", responseEntity.getBody());
    }

    @Test
    public void testProcessQuestionPaper_EmptySubtopics() {
        QuestionPaperDTO request = new QuestionPaperDTO();
        request.setSubject("Math");
        request.setSubTopic(Collections.emptyList());
        request.setLevels(Map.of("Easy", 10, "Medium", 20, "Hard", 5));

        ResponseEntity<?> responseEntity = questionPaperController.processQuestionPaper(request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Subtopics cannot be empty", responseEntity.getBody());
    }


    //positive testcase for getquestionid by assessment id
    @Test
    public void testGetQuestionIdsByAssessmentId_Positive() throws QuestionPaperNotFoundException, QuestionPaperNotFoundException {
        Long assessmentId = 1L;

        // Mock the service method
        when(questionPaperService.getQuestionPaperByAssessmentId(assessmentId)).thenReturn(sampleQuestionPaper);

        // Call the controller method
        ResponseEntity<?> responseEntity = questionPaperController.getQuestionIdsByAssessmentId(assessmentId);

        // Construct the expected response
        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("questionPaperId", sampleQuestionPaper.getQuestionPaperId());
        expectedResponse.put("questionNumbers", sampleQuestionPaper.getQuestionIds());

        // Asserting the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());

        // Verify the interaction with the service
        verify(questionPaperService, times(1)).getQuestionPaperByAssessmentId(assessmentId);
    }


    //Negative testcase for getquestionid by assessment id

    @Test
    public void testGetQuestionIdsByAssessmentId_NotFound() throws QuestionPaperNotFoundException {
        Long assessmentId = 999L; // Assuming 999L is an ID that does not exist

        // Mock the service to throw the exception
        when(questionPaperService.getQuestionPaperByAssessmentId(assessmentId)).thenThrow(new QuestionPaperNotFoundException("Question Paper with assessment ID " + assessmentId + " not found"));

        // Call the controller method
        ResponseEntity<?> responseEntity = questionPaperController.getQuestionIdsByAssessmentId(assessmentId);

        // Asserting the response
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Question Paper with assessment ID " + assessmentId + " not found", responseEntity.getBody());

        // Verify the interaction with the service
        verify(questionPaperService, times(1)).getQuestionPaperByAssessmentId(assessmentId);
    }

    @Test
    public void testGetQuestionIdsByAssessmentId_InvalidId() {
        // Assuming invalid ID is null
        Long assessmentId = null;

        // Call the controller method with invalid ID
        ResponseEntity<?> responseEntity = questionPaperController.getQuestionIdsByAssessmentId(assessmentId);

        // Asserting the response
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid assessment ID", responseEntity.getBody());
    }

    // positive testcase for GetQuestionSplitCountAndDifficultyLevelSplitByAssessmentId
    @Test
    public void testGetQuestionSplitCountAndDifficultyLevelSplitByAssessmentId_Positive() throws QuestionPaperNotFoundException {
        Long assessmentId = 1L;

        // Mock the service method
        when(questionPaperService.getQuestionPaperByAssessmentId(assessmentId)).thenReturn(sampleQuestionPaper);

        // Call the controller method
        ResponseEntity<?> responseEntity = questionPaperController.getQuestionSplitCountAnddifficultyLevelSplitByAssessmentId(assessmentId);

        // Construct the expected response
        Map<String, Object> expectedResponse = new LinkedHashMap<>();
        expectedResponse.put("questionSplitCount", sampleQuestionPaper.getQuestionsSplitCount());
        expectedResponse.put("difficultyLevelSplit", sampleQuestionPaper.getDifficultyLevelSplit());

        // Asserting the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());

        // Verify the interaction with the service
        verify(questionPaperService, times(1)).getQuestionPaperByAssessmentId(assessmentId);
    }

    //negative testcase for GetQuestionSplitCountAndDifficultyLevelSplitByAssessmentId

    @Test
    public void testGetQuestionSplitCountAndDifficultyLevelSplitByAssessmentId_NotFound() throws QuestionPaperNotFoundException {
        Long assessmentId = 999L;

        // Mock the service to throw an exception
        when(questionPaperService.getQuestionPaperByAssessmentId(assessmentId)).thenThrow(new QuestionPaperNotFoundException("Question Paper with assessment ID " + assessmentId + " not found"));

        // Call the controller method
        ResponseEntity<?> responseEntity = questionPaperController.getQuestionSplitCountAnddifficultyLevelSplitByAssessmentId(assessmentId);

        // Asserting the response
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Question Paper with " + assessmentId + " not found", responseEntity.getBody());

        // Verify the interaction with the service
        verify(questionPaperService, times(1)).getQuestionPaperByAssessmentId(assessmentId);
    }

//posiitve for GetQuestionPaperDetailsByAssessmentId
    @Test
    public void testGetQuestionPaperDetailsByAssessmentId_Positive() throws QuestionPaperNotFoundException, QuestionNotFoundException {
        // Mocking dependencies
        QuestionPaperService questionPaperService = mock(QuestionPaperService.class);
        QuestionService questionService = mock(QuestionService.class);

        // Creating a sample question paper
        QuestionPaper questionPaper = new QuestionPaper();
        questionPaper.setQuestionIds(Arrays.asList(1L, 2L, 3L));

        // Creating sample questions
        Question question1 = new Question();
        question1.setQuestionId(1L);
        question1.setTitle("What is the capital of France?");
        Map<String, String> options1 = new LinkedHashMap<>();
        options1.put("A", "London");
        options1.put("B", "Berlin");
        options1.put("C", "Paris");
        question1.setOptions(options1);
        question1.setAnswers(Collections.singletonList("C"));

        Question question2 = new Question();
        question2.setQuestionId(2L);
        question2.setTitle("What is 2 + 2?");
        Map<String, String> options2 = new LinkedHashMap<>();
        options2.put("A", "3");
        options2.put("B", "4");
        options2.put("C", "5");
        question2.setOptions(options2);
        question2.setAnswers(Collections.singletonList("B"));

        // Mocking behavior of questionPaperService
        when(questionPaperService.getQuestionPaperByAssessmentId(1L)).thenReturn(questionPaper);

        // Mocking behavior of questionService
        when(questionService.getQuestionById(1L)).thenReturn(question1);
        when(questionService.getQuestionById(2L)).thenReturn(question2);
        when(questionService.getQuestionById(3L)).thenReturn(null); // Simulating a scenario where a question is not found

        // Creating the controller instance
        QuestionPaperController controller = new QuestionPaperController(questionPaperService, questionService);

        // Invoking the method under test
        ResponseEntity<Map<String, Object>> responseEntity = controller.getQuestionPaperDetailsByAssessmentId(1L);

        // Validating the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Map<String, Object> responseBody = responseEntity.getBody();
        assertNotNull(responseBody);

        List<Map<String, Object>> questions = (List<Map<String, Object>>) responseBody.get("questions");
        assertNotNull(questions);
        assertEquals(2, questions.size()); // Ensuring only two questions are fetched successfully

        // Validating the details of the first question
        Map<String, Object> question1Details = questions.get(0);
        assertEquals("What is the capital of France?", question1Details.get("title"));
        assertEquals(options1, question1Details.get("options"));
        assertEquals(Collections.singletonList("C"), question1Details.get("answers"));

        // Validating the details of the second question
        Map<String, Object> question2Details = questions.get(1);
        assertEquals("What is 2 + 2?", question2Details.get("title"));
        assertEquals(options2, question2Details.get("options"));
        assertEquals(Collections.singletonList("B"), question2Details.get("answers"));
    }

//negative for GetQuestionPaperDetailsByAssessmentId
@Test
public void testGetQuestionPaperDetailsByAssessmentId_Negative() throws QuestionPaperNotFoundException, QuestionNotFoundException {
    // Mocking dependencies
    QuestionPaperService questionPaperService = mock(QuestionPaperService.class);
    QuestionService questionService = mock(QuestionService.class);

    // Mocking behavior of questionPaperService to return null, simulating scenario where question paper is not found
    when(questionPaperService.getQuestionPaperByAssessmentId(1L)).thenThrow(new QuestionPaperNotFoundException("Question paper not found with Assessment ID: 1"));

    // Creating the controller instance
    QuestionPaperController controller = new QuestionPaperController(questionPaperService, questionService);

    // Invoking the method under test with an assessment ID for which the question paper does not exist and expecting an exception
    Throwable exception = assertThrows(QuestionPaperNotFoundException.class, () -> {
        controller.getQuestionPaperDetailsByAssessmentId(1L);
    });

    // Validating the exception message
    assertEquals("Question paper not found with Assessment ID: 1", exception.getMessage());
}


    @Test
    public void testGetQuestionPaperDetailsByAssessmentId_InvalidAssessmentId() throws QuestionPaperNotFoundException, QuestionNotFoundException {
        // Mocking dependencies
        QuestionPaperService questionPaperService = mock(QuestionPaperService.class);
        QuestionService questionService = mock(QuestionService.class);

        // Mocking behavior of questionPaperService to return null for an invalid assessment ID
        when(questionPaperService.getQuestionPaperByAssessmentId(999L)).thenReturn(null);

        // Creating the controller instance
        QuestionPaperController controller = new QuestionPaperController(questionPaperService, questionService);

        // Invoking the method under test with an invalid assessment ID and expecting a QuestionPaperNotFoundException
        Throwable exception = assertThrows(QuestionPaperNotFoundException.class, () -> {
            controller.getQuestionPaperDetailsByAssessmentId(999L);
        });

        // Validating the exception message
        assertEquals("Question paper not found with Assessment ID: 999", exception.getMessage());
    }

    //positive for DeleteQuestionPaperByAssessmentId

    @Test
    public void testDeleteQuestionPaperByAssessmentId_Positive() throws QuestionNotFoundException, QuestionPaperNotFoundException {
        // Mocking dependencies
        QuestionPaperService questionPaperService = mock(QuestionPaperService.class);

        QuestionService questionService1 = mock(QuestionService.class);

        // Mocking behavior of questionPaperService
        doNothing().when(questionPaperService).deleteQuestionPaperByAssessmentId(anyLong()); // Mocking any assessment ID

        // Creating the controller instance
        QuestionPaperController controller = new QuestionPaperController(questionPaperService,questionService1);

        // Invoking the method under test with a valid assessment ID
        ResponseEntity<?> responseEntity = controller.deleteQuestionPaperByAssessmentId(1L);

        // Validating the response
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    //Negative for DeleteQuestionPaperByAssessmentId
    @Test
    public void testDeleteQuestionPaperByAssessmentId_Negative_QuestionPaperNotFound() throws QuestionNotFoundException, QuestionPaperNotFoundException {
        // Mocking dependencies
        QuestionPaperService questionPaperService = mock(QuestionPaperService.class);

        QuestionService questionService1 = mock(QuestionService.class);

        // Mocking behavior of questionPaperService to throw QuestionPaperNotFoundException
        doThrow(QuestionPaperNotFoundException.class).when(questionPaperService).deleteQuestionPaperByAssessmentId(anyLong());

        // Creating the controller instance
        QuestionPaperController controller = new QuestionPaperController(questionPaperService,questionService1);

        // Invoking the method under test with a valid assessment ID and expecting a response indicating QuestionPaperNotFoundException
        ResponseEntity<?> responseEntity = controller.deleteQuestionPaperByAssessmentId(1L);

        // Validating the response
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Question paper not found with Assessment ID: 1", "Question paper not found with Assessment ID: 1");
    }

    @Test
    public void testDeleteQuestionPaperByAssessmentId_Negative_ExceptionThrown() throws QuestionNotFoundException, QuestionPaperNotFoundException {
        // Mocking dependencies
        QuestionPaperService questionPaperService = mock(QuestionPaperService.class);

        QuestionService questionService1 = mock(QuestionService.class);

        // Mocking behavior of questionPaperService to throw a general Exception
        doThrow(RuntimeException.class).when(questionPaperService).deleteQuestionPaperByAssessmentId(anyLong());

        // Creating the controller instance
        QuestionPaperController controller = new QuestionPaperController(questionPaperService, questionService1);

        // Invoking the method under test with a valid assessment ID and expecting a response indicating internal server error
        ResponseEntity<?> responseEntity = controller.deleteQuestionPaperByAssessmentId(1L);

        // Validating the response
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("An error occurred while deleting the question paper.", responseEntity.getBody());
    }




}

