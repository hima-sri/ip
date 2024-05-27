package com.thbs.questionMS.service;

import com.thbs.questionMS.dto.QuestionPaperDTO;
import com.thbs.questionMS.exception.QuestionNotFoundException;
import com.thbs.questionMS.exception.QuestionPaperNotFoundException;
import com.thbs.questionMS.exception.UnableToGenerateQuestionPaperException;
import com.thbs.questionMS.model.Question;
import com.thbs.questionMS.model.QuestionPaper;
import com.thbs.questionMS.repository.QuestionPaperRepository;
import com.thbs.questionMS.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionPaperService {

    private final QuestionRepository questionRepository;
    private SequenceGeneratorService sequenceGeneratorService;
    private final QuestionPaperRepository questionPaperRepository;

    @Autowired
    public QuestionPaperService(SequenceGeneratorService sequenceGeneratorService,QuestionPaperRepository questionPaperRepository,QuestionRepository questionRepository) {
        this.questionPaperRepository = questionPaperRepository;
        this.questionRepository = questionRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }
//method to fetch the questionIds by using filters like Subject, Subtopics, level, count
    public List<Long> getQuestionIdsBySubjectSubtopicAndLevel(String subject, String subtopic, String level, int count) {
        List<Question> questions = questionRepository.findBySubjectAndSubtopicAndLevel(subject, subtopic, level);

        // Shuffle the list of questions
        Collections.shuffle(questions);

        // Filter questions with count greater than 5 and limit to count
        List<Long> questionIds = questions.stream()
                .filter(question -> question.getUsageCount() < 15)
                .limit(count)
                .map(question -> {
                    // Increment usage count for selected question
                    question.setUsageCount(question.getUsageCount() + 1);
                    return question.getQuestionId();
                })
                .collect(Collectors.toList());

        // Save updated questions to the database
        questionRepository.saveAll(questions);

        return questionIds;
    }

    //
    public QuestionPaper getQuestionPaperByQuestionPaperId(Long id) throws QuestionPaperNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        Optional<QuestionPaper> questionPaperOptional = questionPaperRepository.findById(id);
        if (questionPaperOptional.isPresent()) {
            return questionPaperOptional.get();
        } else {
            throw new QuestionPaperNotFoundException("Question paper not found with ID: " + id);
        }
    }

    //method to finalize the questionPaper like to save the QP and check the constraints to throw the exceptions

public List<Map<String, Object>> processQuestionPaper(QuestionPaperDTO request) throws UnableToGenerateQuestionPaperException {
    String subject = request.getSubject();
    List<String> subTopics = request.getSubTopic();
    Map<String, Integer> levels = request.getLevels();
    Long assessmentId = request.getAssessmentId();

    // Check if any field is empty
    if (subject == null || assessmentId == null || levels == null || subTopics == null ||
            subject.isEmpty() || levels.isEmpty() || subTopics.isEmpty()) {
        throw new UnableToGenerateQuestionPaperException("All fields in the request are required.\nProvide: " +
                (assessmentId == null ? "AssessmentId " : "") +
                (subject == null || subject.isEmpty() ? "Subject " : "") +
                (subTopics == null || subTopics.isEmpty() ? "Subtopics " : "") +
                (levels == null || levels.isEmpty() ? "DifficultyLevels" : ""));
    }

    // Check if assessmentId already exists in the database
    if (questionPaperRepository.existsByAssessmentId(assessmentId)) {
        throw new UnableToGenerateQuestionPaperException("Assessment ID " + assessmentId + " already exists. Please provide a unique assessment ID.");
    }

    List<Long> allQuestionIds = new ArrayList<>();
    Map<String, Map<String, List<Long>>> subTopicsMap = new HashMap<>();
    Map<String, Map<String, Integer>> countMap = new HashMap<>();

    for (String subTopic : subTopics) {
        Map<String, List<Long>> subTopicData = new HashMap<>();
        Map<String, Integer> countMapData = new HashMap<>();
        // Calculate distribution of questions into levels
        int easyCount = levels.get("Easy") / subTopics.size();
        int mediumCount = levels.get("Medium") / subTopics.size();
        int hardCount = levels.get("Hard") / subTopics.size();

        // Handle remaining questions (if any)
        int remainingEasy = levels.get("Easy") % subTopics.size();
        int remainingMedium = levels.get("Medium") % subTopics.size();
        int remainingHard = levels.get("Hard") % subTopics.size();

        // Adjust counts for remaining questions
        if (subTopics.indexOf(subTopic) < remainingEasy) {
            easyCount++;
        }
        if (subTopics.indexOf(subTopic) < remainingMedium) {
            mediumCount++;
        }
        if (subTopics.indexOf(subTopic) < remainingHard) {
            hardCount++;
        }

        // Retrieve question IDs for each level
        List<Long> easyQuestionIds = getQuestionIdsBySubjectSubtopicAndLevel(subject, subTopic, "Easy", easyCount);
        List<Long> mediumQuestionIds = getQuestionIdsBySubjectSubtopicAndLevel(subject, subTopic, "Medium", mediumCount);
        List<Long> hardQuestionIds = getQuestionIdsBySubjectSubtopicAndLevel(subject, subTopic, "Hard", hardCount);

        if (easyQuestionIds.size() < easyCount) {
            throw new UnableToGenerateQuestionPaperException("Unable to generate question paper for subject: " + subject +
                    ", subtopic: " + subTopic + ", level: Easy. Not enough questions available for Easy level.");
        } else if (mediumQuestionIds.size() < mediumCount) {
            throw new UnableToGenerateQuestionPaperException("Unable to generate question paper for subject: " + subject +
                    ", subtopic: " + subTopic + ", level: Medium. Not enough questions available for Medium level.");
        } else if (hardQuestionIds.size() < hardCount) {
            throw new UnableToGenerateQuestionPaperException("Unable to generate question paper for subject: " + subject +
                    ", subtopic: " + subTopic + ", level: Hard. Not enough questions available for Hard level.");
        }

        List<Long> allQuestionSubTopicIds = new ArrayList<>();
        allQuestionSubTopicIds.addAll(easyQuestionIds);
        allQuestionSubTopicIds.addAll(mediumQuestionIds);
        allQuestionSubTopicIds.addAll(hardQuestionIds);

        allQuestionIds.addAll(allQuestionSubTopicIds);

        // Populate subtopic data
        subTopicData.put("Easy", easyQuestionIds);
        subTopicData.put("Medium", mediumQuestionIds);
        subTopicData.put("Hard", hardQuestionIds);

        countMapData.put("Easy", easyCount);
        countMapData.put("Medium", mediumCount);
        countMapData.put("Hard", hardCount);

        subTopicsMap.put(subTopic, subTopicData);
        countMap.put(subTopic, countMapData);
    }

    // Save the question paper details
    QuestionPaper questionPaper = new QuestionPaper();
    questionPaper.setQuestionPaperId(sequenceGeneratorService.generateSequence("question_paper_sequence"));
    questionPaper.setSubject(subject);
    questionPaper.setQuestionsSplitIds(subTopicsMap);
    questionPaper.setQuestionsSplitCount(countMap);
    questionPaper.setQuestionIds(allQuestionIds);
    questionPaper.setDifficultyLevelSplit(levels);
    questionPaper.setAssessmentId(assessmentId);
    questionPaperRepository.save(questionPaper);

    // Retrieve questions with options
    List<Question> questions = questionRepository.findAllById(allQuestionIds);

    // Extract only titles and options
    List<Map<String, Object>> questionDetails = questions.stream()
            .map(question -> {
                Map<String, Object> questionDetail = new HashMap<>();
                questionDetail.put("title", question.getTitle());
                questionDetail.put("options", question.getOptions());
                return questionDetail;
            })
            .collect(Collectors.toList());

    return questionDetails;
}


// method to fetch the QuestionPaper by assessmentId
    public QuestionPaper getQuestionPaperByAssessmentId(Long assessmentId) throws QuestionPaperNotFoundException {
        try {
            Optional<QuestionPaper> questionPaperOptional = questionPaperRepository.findByAssessmentId(assessmentId);
            return questionPaperOptional.orElseThrow(() -> new QuestionPaperNotFoundException("Question paper not found with Assessment ID: " + assessmentId));
        } catch (NoSuchElementException ex) {
            throw new QuestionPaperNotFoundException("Question paper not found with Assessment ID: " + assessmentId);
        }
    }

    //method to delete the QP by assessmentId

    public void deleteQuestionPaperByAssessmentId(Long assessmentId) throws QuestionPaperNotFoundException, QuestionNotFoundException {
        if (assessmentId == null) {
            throw new IllegalArgumentException("Assessment ID cannot be null");
        }
        Optional<QuestionPaper> questionPaperOptional = questionPaperRepository.findByAssessmentId(assessmentId);

        if (!questionPaperOptional.isPresent()) {
            throw new QuestionPaperNotFoundException("Question paper not found with Assessment ID: " + assessmentId);
        }

        QuestionPaper questionPaper = questionPaperOptional.get();
        List<Long> questionIds = questionPaper.getQuestionIds();

        // Decrement the usage count for each question
        for (Long questionId : questionIds) {
            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new QuestionNotFoundException("Question not found with ID: " + questionId));
            question.setUsageCount(question.getUsageCount() - 1);
            questionRepository.save(question);
        }

        // Delete the question paper
        questionPaperRepository.delete(questionPaper);
    }




}
