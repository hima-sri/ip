package com.thbs.questionMS.dto;

import java.util.List;
import java.util.Map;

public class QuestionPaperDTO {

    private Long assessmentId;

    private String subject;
    private List<String> subTopic;
    private Map<String, Integer> levels; // Assuming levels include easy, medium, and hard
    private Map<String, Map<String, Integer>> counts; // Processed data for subTopics

    public QuestionPaperDTO() {
    }

    public QuestionPaperDTO(String subject, List<String> subTopic, Map<String, Integer> levels) {
        this.subject = subject;
        this.subTopic = subTopic;
        this.levels = levels;
    }


    public Long getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(Long assessmentId) {
        this.assessmentId = assessmentId;
    }
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<String> getSubTopic() {
        return subTopic;
    }

    public void setSubTopic(List<String> subTopic) {
        this.subTopic = subTopic;
    }

    public Map<String, Integer> getLevels() {
        return levels;
    }

    public void setLevels(Map<String, Integer> levels) {
        this.levels = levels;
    }

    public Map<String, Map<String, Integer>> getCounts() {
        return counts;
    }

    public void setCounts(Map<String, Map<String, Integer>> counts) {
        this.counts = counts;
    }
}
