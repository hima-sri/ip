package com.thbs.questionMS.ServiceTests;

import com.thbs.questionMS.dto.QuestionPaperDTO;
import com.thbs.questionMS.exception.QuestionNotFoundException;
import com.thbs.questionMS.exception.QuestionPaperNotFoundException;
import com.thbs.questionMS.exception.UnableToGenerateQuestionPaperException;
import com.thbs.questionMS.model.Question;
import com.thbs.questionMS.model.QuestionPaper;
import com.thbs.questionMS.repository.QuestionPaperRepository;
import com.thbs.questionMS.repository.QuestionRepository;
import com.thbs.questionMS.service.QuestionPaperService;
import com.thbs.questionMS.service.SequenceGeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class QuestionPaperServiceTests {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private SequenceGeneratorService sequenceGeneratorService;
    @Mock
    private QuestionPaperRepository questionPaperRepository;

    @InjectMocks
    private QuestionPaperService questionPaperService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //positive testcase for GETBYSUBJECTANDSUBTOPIC

    @Test
    void testGetQuestionIdsBySubjectSubtopicAndLevel_Positive() {
        // Arrange
        String subject = "Mathematics";
        String subtopic = "Algebra";
        String level = "Easy";
        int count = 3;

        Question question1 = new Question();
        question1.setQuestionId(1L);
        question1.setUsageCount(10);
        question1.setSubject(subject);
        question1.setSubTopic(subtopic);
        question1.setLevel(level);

        Question question2 = new Question();
        question2.setQuestionId(2L);
        question2.setUsageCount(5);
        question2.setSubject(subject);
        question2.setSubTopic(subtopic);
        question2.setLevel(level);

        Question question3 = new Question();
        question3.setQuestionId(3L);
        question3.setUsageCount(0);
        question3.setSubject(subject);
        question3.setSubTopic(subtopic);
        question3.setLevel(level);

        List<Question> questions = Arrays.asList(question1, question2, question3);

        when(questionRepository.findBySubjectAndSubtopicAndLevel(subject, subtopic, level)).thenReturn(questions);

        // Act
        List<Long> result = questionPaperService.getQuestionIdsBySubjectSubtopicAndLevel(subject, subtopic, level, count);

        // Assert
        List<Long> expected = Arrays.asList(1L, 2L, 3L);
        assertTrue(result.containsAll(expected) && expected.containsAll(result));
        verify(questionRepository, times(1)).findBySubjectAndSubtopicAndLevel(subject, subtopic, level);
        verify(questionRepository, times(1)).saveAll(questions);
    }

    //NEGATIVE testcase for GETBYSUBJECTANDSUBTOPIC
    @Test
    void testGetQuestionIdsBySubjectSubtopicAndLevel_NotEnoughQuestions() {
        // Arrange
        String subject = "Mathematics";
        String subtopic = "Algebra";
        String level = "Easy";
        int count = 5;

        Question question1 = new Question();
        question1.setQuestionId(1L);
        question1.setUsageCount(10);
        question1.setSubject(subject);
        question1.setSubTopic(subtopic);
        question1.setLevel(level);

        List<Question> questions = Collections.singletonList(question1);

        when(questionRepository.findBySubjectAndSubtopicAndLevel(subject, subtopic, level)).thenReturn(questions);

        // Act
        List<Long> result = questionPaperService.getQuestionIdsBySubjectSubtopicAndLevel(subject, subtopic, level, count);

        // Assert
        assertTrue(result.size() < count);
        verify(questionRepository, times(1)).findBySubjectAndSubtopicAndLevel(subject, subtopic, level);
        verify(questionRepository, times(1)).saveAll(questions);
    }

    @Test
    void testGetQuestionIdsBySubjectSubtopicAndLevel_HighUsageCount() {
        // Arrange
        String subject = "Mathematics";
        String subtopic = "Algebra";
        String level = "Easy";
        int count = 3;

        Question question1 = new Question();
        question1.setQuestionId(1L);
        question1.setUsageCount(16);
        question1.setSubject(subject);
        question1.setSubTopic(subtopic);
        question1.setLevel(level);

        Question question2 = new Question();
        question2.setQuestionId(2L);
        question2.setUsageCount(17);
        question2.setSubject(subject);
        question2.setSubTopic(subtopic);
        question2.setLevel(level);

        Question question3 = new Question();
        question3.setQuestionId(3L);
        question3.setUsageCount(18);
        question3.setSubject(subject);
        question3.setSubTopic(subtopic);
        question3.setLevel(level);

        List<Question> questions = Arrays.asList(question1, question2, question3);

        when(questionRepository.findBySubjectAndSubtopicAndLevel(subject, subtopic, level)).thenReturn(questions);

        // Act
        List<Long> result = questionPaperService.getQuestionIdsBySubjectSubtopicAndLevel(subject, subtopic, level, count);

        // Assert
        assertTrue(result.isEmpty());
        verify(questionRepository, times(1)).findBySubjectAndSubtopicAndLevel(subject, subtopic, level);
        verify(questionRepository, times(1)).saveAll(questions);
    }

    @Test
    void testGetQuestionIdsBySubjectSubtopicAndLevel_NoQuestionsAvailable() {
        // Arrange
        String subject = "Mathematics";
        String subtopic = "Algebra";
        String level = "Easy";
        int count = 3;

        when(questionRepository.findBySubjectAndSubtopicAndLevel(subject, subtopic, level)).thenReturn(Collections.emptyList());

        // Act
        List<Long> result = questionPaperService.getQuestionIdsBySubjectSubtopicAndLevel(subject, subtopic, level, count);

        // Assert
        assertTrue(result.isEmpty());
        verify(questionRepository, times(1)).findBySubjectAndSubtopicAndLevel(subject, subtopic, level);
    }

    @Test
    void testGetQuestionIdsBySubjectSubtopicAndLevel_RepositoryException() {
        // Arrange
        String subject = "Mathematics";
        String subtopic = "Algebra";
        String level = "Easy";
        int count = 3;

        when(questionRepository.findBySubjectAndSubtopicAndLevel(subject, subtopic, level)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> questionPaperService.getQuestionIdsBySubjectSubtopicAndLevel(subject, subtopic, level, count));
        verify(questionRepository, times(1)).findBySubjectAndSubtopicAndLevel(subject, subtopic, level);
    }

    //postive testGetQuestionPaperByQuestionPaperId
    @Test
    void testGetQuestionPaperByQuestionPaperId_Success() throws QuestionPaperNotFoundException {
        // Arrange
        Long id = 1L;
        QuestionPaper expectedQuestionPaper = new QuestionPaper();
        expectedQuestionPaper.setQuestionPaperId(id);
//        expectedQuestionPaper.set("Sample Question Paper");

        when(questionPaperRepository.findById(id)).thenReturn(Optional.of(expectedQuestionPaper));

        // Act
        QuestionPaper result = questionPaperService.getQuestionPaperByQuestionPaperId(id);

        // Assert
        assertEquals(expectedQuestionPaper, result);
        verify(questionPaperRepository, times(1)).findById(id);
    }
    //postive testGetQuestionPaperByQuestionPaperId




    @Test
    void testGetQuestionPaperByQuestionPaperId_QuestionPaperNotFound() {
        // Arrange
        Long id = 999L;
        when(questionPaperRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(QuestionPaperNotFoundException.class, () -> questionPaperService.getQuestionPaperByQuestionPaperId(id));
        verify(questionPaperRepository, times(1)).findById(id);
    }


    @Test
    void testGetQuestionPaperByQuestionPaperId_NullId() {
        // Arrange
        Long id = null;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> questionPaperService.getQuestionPaperByQuestionPaperId(id));
        assertEquals("ID cannot be null", exception.getMessage());
        verify(questionPaperRepository, never()).findById(any());
    }

    @Test
    void testGetQuestionPaperByQuestionPaperId_RepositoryException() {
        // Arrange
        Long id = 1L;
        when(questionPaperRepository.findById(id)).thenThrow(new RuntimeException("Database access error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> questionPaperService.getQuestionPaperByQuestionPaperId(id));
        assertEquals("Database access error", exception.getMessage());
        verify(questionPaperRepository, times(1)).findById(id);
    }





//positive testcase for getQuestionPaperByQuestionPaperId
    @Test
    void testGetQuestionPaperByAssessmentId_ValidId() {
        // Arrange
        Long assessmentId = 123L;
        QuestionPaper questionPaper = new QuestionPaper();
        // Set up the mock behavior to return an Optional containing the questionPaper
        when(questionPaperRepository.findByAssessmentId(assessmentId)).thenReturn(Optional.of(questionPaper));

        // Act
        QuestionPaper retrievedQuestionPaper = null;
        try {
            retrievedQuestionPaper = questionPaperService.getQuestionPaperByAssessmentId(assessmentId);
        } catch (QuestionPaperNotFoundException e) {
            fail("Unexpected QuestionPaperNotFoundException: " + e.getMessage());
        }

        // Assert
        assertNotNull(retrievedQuestionPaper);
        assertEquals(questionPaper, retrievedQuestionPaper);
        verify(questionPaperRepository, times(1)).findByAssessmentId(assessmentId);
    }




//negative testcase for getQuestionPaperByQuestionPaperId
    @Test
    void testGetQuestionPaperByAssessmentId_IdNotFound() {
        // Arrange
        Long assessmentId = 123L;
        when(questionPaperRepository.findByAssessmentId(assessmentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(QuestionPaperNotFoundException.class, () -> questionPaperService.getQuestionPaperByAssessmentId(assessmentId));
        verify(questionPaperRepository, times(1)).findByAssessmentId(assessmentId);
    }

    @Test
    void testGetQuestionPaperByAssessmentId_NoSuchElementException() {
        // Arrange
        Long assessmentId = 456L;
        when(questionPaperRepository.findByAssessmentId(assessmentId)).thenThrow(new NoSuchElementException("Repository exception"));

        // Act & Assert
        assertThrows(QuestionPaperNotFoundException.class, () -> questionPaperService.getQuestionPaperByAssessmentId(assessmentId));
        verify(questionPaperRepository, times(1)).findByAssessmentId(assessmentId);
    }

    @Test
        void testGetQuestionPaperByAssessmentId_NonExistentId() {
        // Arrange
        Long nonExistentId = 999L;
        when(questionPaperRepository.findByAssessmentId(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(QuestionPaperNotFoundException.class, () -> questionPaperService.getQuestionPaperByAssessmentId(nonExistentId));
        verify(questionPaperRepository, times(1)).findByAssessmentId(nonExistentId);
    }



    //positive test for DeleteQuestionPaperByAssessmentId
    @Test
    void testDeleteQuestionPaperByAssessmentId_ValidId() {
        // Arrange
        Long assessmentId = 123L;
        QuestionPaper questionPaper = new QuestionPaper();
        questionPaper.setAssessmentId(assessmentId);
        List<Long> questionIds = Arrays.asList(1L, 2L, 3L); // Example question IDs
        questionPaper.setQuestionIds(questionIds);

        when(questionPaperRepository.findByAssessmentId(assessmentId)).thenReturn(Optional.of(questionPaper));
        for (Long questionId : questionIds) {
            when(questionRepository.findById(questionId)).thenReturn(Optional.of(new Question()));
        }

        // Act
        assertDoesNotThrow(() -> questionPaperService.deleteQuestionPaperByAssessmentId(assessmentId));

        // Assert
        verify(questionPaperRepository, times(1)).findByAssessmentId(assessmentId);
        for (Long questionId : questionIds) {
            verify(questionRepository, times(1)).findById(questionId);
        }
        verify(questionPaperRepository, times(1)).delete(questionPaper);
    }


    //negative testcase for delete qp by assessment id

    @Test
    void testDeleteQuestionPaperByAssessmentId_NullId() {
        // Arrange

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> questionPaperService.deleteQuestionPaperByAssessmentId(null));

        // Assert
        verify(questionPaperRepository, never()).findByAssessmentId(any());
        verify(questionRepository, never()).findById(any());
        verify(questionPaperRepository, never()).delete(any());
    }


    @Test
    void testDeleteQuestionPaperByAssessmentId_NonExistentPaper() {
        // Arrange
        Long assessmentId = 123L;
        when(questionPaperRepository.findByAssessmentId(assessmentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(QuestionPaperNotFoundException.class, () -> questionPaperService.deleteQuestionPaperByAssessmentId(assessmentId));
        verify(questionPaperRepository, times(1)).findByAssessmentId(assessmentId);
        verify(questionRepository, never()).findById(any());
        verify(questionPaperRepository, never()).delete(any());
    }

    @Test
    void testDeleteQuestionPaperByAssessmentId_NonExistentAssessmentId() {
        // Arrange
        Long assessmentId = 123L;
        when(questionPaperRepository.findByAssessmentId(assessmentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(QuestionPaperNotFoundException.class, () -> questionPaperService.deleteQuestionPaperByAssessmentId(assessmentId));
        verify(questionPaperRepository, times(1)).findByAssessmentId(assessmentId);
        verify(questionRepository, never()).findById(any());
        verify(questionPaperRepository, never()).delete(any());
    }



    @Test
    void testDeleteQuestionPaperByAssessmentId_RepositoryException() {
        // Arrange
        Long assessmentId = 123L;
        when(questionPaperRepository.findByAssessmentId(assessmentId)).thenThrow(new RuntimeException("Repository exception"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> questionPaperService.deleteQuestionPaperByAssessmentId(assessmentId));
        verify(questionPaperRepository, times(1)).findByAssessmentId(assessmentId);
        verify(questionRepository, never()).findById(any());
        verify(questionPaperRepository, never()).delete(any());
    }


//positive tests for processs quesytion paper


    @Test
    void testProcessQuestionPaper_PositiveCase() throws UnableToGenerateQuestionPaperException {
        // Arrange
        QuestionPaperDTO request = new QuestionPaperDTO();
        request.setSubject("Mathematics");
        request.setSubTopic(Arrays.asList("Algebra", "Geometry"));
        request.setLevels(Map.of("Easy", 0, "Medium", 0,"Hard", 0));
        request.setAssessmentId(123L);

        // Mocking repository methods
        when(questionPaperRepository.existsByAssessmentId(request.getAssessmentId())).thenReturn(false);
        when(sequenceGeneratorService.generateSequence("question_paper_sequence")).thenReturn(1L);

        // Mocking question repository to return dummy questions
        List<Question> dummyQuestions = Arrays.asList(
                new Question("Mathematics", "Algebra", "Question 1",  new LinkedHashMap<>(), "Easy", "type", "answerType", Arrays.asList("Answer 1", "Answer 2")),
                new Question("Mathematics", "Algebra", "Question 2",  new LinkedHashMap<>(), "Medium", "type", "answerType", Arrays.asList("Answer 3", "Answer 4")),
                new Question("Mathematics", "Geometry", "Question 3",  new LinkedHashMap<>(), "Hard", "type", "answerType", Arrays.asList("Answer 5", "Answer 6")),
                // Add more dummy questions for the "Easy" level under the "Algebra" subtopic
                new Question("Mathematics", "Algebra", "Question 4",  new LinkedHashMap<>(), "Easy", "type", "answerType", Arrays.asList("Answer 7", "Answer 8")),
                new Question("Mathematics", "Algebra", "Question 5",  new LinkedHashMap<>(), "Easy", "type", "answerType", Arrays.asList("Answer 9", "Answer 10"))
        );
        List<Long> questionIds = dummyQuestions.stream().map(Question::getQuestionId).collect(Collectors.toList());
        when(questionRepository.findAllById(questionIds)).thenReturn(dummyQuestions);

        // Act
        List<Map<String, Object>> result = questionPaperService.processQuestionPaper(request);

        // Assert
        assertEquals(0, result.size());


    }



    //negative testcase for processqp

    @Test
    void testProcessQuestionPaper_EmptySubject() {
        // Arrange
        QuestionPaperDTO request = new QuestionPaperDTO();
        request.setSubject(""); // Empty subject
        request.setSubTopic(Arrays.asList("Algebra", "Geometry"));
        request.setLevels(Map.of("Easy", 5, "Medium", 5, "Hard", 5));
        request.setAssessmentId(123L);

        // Act & Assert
        assertThrows(UnableToGenerateQuestionPaperException.class, () -> questionPaperService.processQuestionPaper(request));
    }


    @Test
    void testProcessQuestionPaper_EmptySubtopics() {
        // Arrange
        QuestionPaperDTO request = new QuestionPaperDTO();
        request.setSubject("Mathematics");
        request.setSubTopic(Collections.emptyList()); // Empty subtopics list
        request.setLevels(Map.of("Easy", 5, "Medium", 5, "Hard", 5));
        request.setAssessmentId(123L);

        // Act & Assert
        assertThrows(UnableToGenerateQuestionPaperException.class, () -> questionPaperService.processQuestionPaper(request));
    }


    @Test
    void testProcessQuestionPaper_ExistingAssessmentId() {
        // Arrange
        QuestionPaperDTO request = new QuestionPaperDTO();
        request.setSubject("Mathematics");
        request.setSubTopic(Arrays.asList("Algebra", "Geometry"));
        request.setLevels(Map.of("Easy", 5, "Medium", 5, "Hard", 5));
        request.setAssessmentId(123L);

        // Mocking repository methods to return true, indicating existing assessment ID
        when(questionPaperRepository.existsByAssessmentId(request.getAssessmentId())).thenReturn(true);

        // Act & Assert
        assertThrows(UnableToGenerateQuestionPaperException.class, () -> questionPaperService.processQuestionPaper(request));
    }


    @Test
    void testProcessQuestionPaper_NotEnoughEasyQuestions() {
        // Arrange
        QuestionPaperDTO request = new QuestionPaperDTO();
        request.setSubject("Mathematics");
        request.setSubTopic(Arrays.asList("Algebra"));
        request.setLevels(Map.of("Easy", 10, "Medium", 5, "Hard", 5)); // Requesting 10 easy questions
        request.setAssessmentId(123L);

        // Mocking repository methods to return empty lists for easy questions
        when(questionRepository.findBySubjectAndSubtopicAndLevel("Mathematics", "Algebra", "Easy"))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(UnableToGenerateQuestionPaperException.class, () -> questionPaperService.processQuestionPaper(request));
    }

    @Test
    void testProcessQuestionPaper_NotEnoughMediumQuestions() {
        // Arrange
        QuestionPaperDTO request = new QuestionPaperDTO();
        request.setSubject("Mathematics");
        request.setSubTopic(Arrays.asList("Algebra"));
        request.setLevels(Map.of("Easy", 0, "Medium", 10, "Hard", 0)); // Requesting 10 medium questions
        request.setAssessmentId(123L);

        // Mocking repository methods to return empty lists for medium questions
        when(questionRepository.findBySubjectAndSubtopicAndLevel("Mathematics", "Algebra", "Medium"))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(UnableToGenerateQuestionPaperException.class, () -> questionPaperService.processQuestionPaper(request));
    }


    @Test
    void testProcessQuestionPaper_NotEnoughHardQuestions() {
        // Arrange
        QuestionPaperDTO request = new QuestionPaperDTO();
        request.setSubject("Mathematics");
        request.setSubTopic(Arrays.asList("Algebra"));
        request.setLevels(Map.of("Easy", 0, "Medium", 0, "Hard", 10)); // Requesting 10 hard questions
        request.setAssessmentId(123L);

        // Mocking repository methods to return empty lists for hard questions
        when(questionRepository.findBySubjectAndSubtopicAndLevel("Mathematics", "Algebra", "Hard"))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(UnableToGenerateQuestionPaperException.class, () -> questionPaperService.processQuestionPaper(request));
    }
}