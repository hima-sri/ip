package com.thbs.questionMS.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.Map;

@Document(collection = "questionPaper")
public class QuestionPaper {

    @Transient
    public static final String SEQUENCE_NAME = "questions_paper_sequence";


    @Id
    private Long questionPaperId;

    private Long assessmentId;

    private String subject;
    private List<Long> questionIds;

    private Map<String, Integer> difficultyLevelSplit; // Assuming levels include easy, medium, and hard

    private Map<String, Map<String, List<Long>>> questionsSplitIds;

    private Map<String, Map<String, Integer>> questionsSplitCount;



    public Map<String, Map<String, Integer>> getQuestionsSplitCount() {
        return questionsSplitCount;
    }

    public void setQuestionsSplitCount(Map<String, Map<String, Integer>> questionsSplitCount) {
        this.questionsSplitCount = questionsSplitCount;
    }

    public Long getQuestionPaperId() {
        return questionPaperId;
    }

    public void setQuestionPaperId(Long questionPaperId) {
        this.questionPaperId = questionPaperId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Map<String, Map<String, List<Long>>> getQuestionsSplitIds() {
        return questionsSplitIds;
    }

    public void setQuestionsSplitIds(Map<String, Map<String, List<Long>>> questionsSplitIds) {
        this.questionsSplitIds = questionsSplitIds;
    }

    public List<Long> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(List<Long> questionIds) {
        this.questionIds = questionIds;
    }
    public Long getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(Long assessmentId) {
        this.assessmentId = assessmentId;
    }

    public Map<String, Integer> getDifficultyLevelSplit() {
        return difficultyLevelSplit;
    }

    public void setDifficultyLevelSplit(Map<String, Integer> difficultyLevelSplit) {
        this.difficultyLevelSplit = difficultyLevelSplit;
    }
}
