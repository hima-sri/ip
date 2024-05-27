package com.quiz.assessment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
public class Assessment {

   

	public Assessment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Assessment(Long assessmentId, String assessmentName, String targetBatch, LocalDateTime startTime,
			LocalDateTime endTime, int durationMinutes, int numberOfQuestions, int passingPercentage, String courseName,
			String assessmentType, Boolean status, Integer numberOfViolations) {
		super();
		this.assessmentId = assessmentId;
		this.assessmentName = assessmentName;
		this.targetBatch = targetBatch;
		this.startTime = startTime;
		this.endTime = endTime;
		this.durationMinutes = durationMinutes;
		this.numberOfQuestions = numberOfQuestions;
		this.passingPercentage = passingPercentage;
		this.courseName = courseName;
		this.assessmentType = assessmentType;
		this.status = status;
		this.numberOfViolations = numberOfViolations;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assessmentId;

    @Column(name="assessment_name",unique = true)
    private String assessmentName;
	
	@Column(name = "target_batch")
    private String targetBatch;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "duration_minutes")
    private int durationMinutes;

    @Column(name = "number_of_questions")
    private int numberOfQuestions;

    @Column(name = "passing_percentage")
    private int passingPercentage;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "assessment_type")
    private String assessmentType;

    @Column(name = "status", columnDefinition = "boolean default true")
    private Boolean status = true;

    @Column(name = "number_of_violations")
    private Integer numberOfViolations;

//    @Column(name ="technology")
//    private String Technology;

	public Long getAssessmentId() {
		return assessmentId;
	}

	public void setAssessmentId(Long assessmentId) {
		this.assessmentId = assessmentId;
	}

	public String getAssessmentName() {
		return assessmentName;
	}

	public void setAssessmentName(String assessmentName) {
		this.assessmentName = assessmentName;
	}

	public String getTargetBatch() {
		return targetBatch;
	}

	public void setTargetBatch(String targetBatch) {
		this.targetBatch = targetBatch;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public int getDurationMinutes() {
		return durationMinutes;
	}

	public void setDurationMinutes(int durationMinutes) {
		this.durationMinutes = durationMinutes;
	}

	public int getNumberOfQuestions() {
		return numberOfQuestions;
	}

	public void setNumberOfQuestions(int numberOfQuestions) {
		this.numberOfQuestions = numberOfQuestions;
	}

	public int getPassingPercentage() {
		return passingPercentage;
	}

	public void setPassingPercentage(int passingPercentage) {
		this.passingPercentage = passingPercentage;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getAssessmentType() {
		return assessmentType;
	}

	public void setAssessmentType(String assessmentType) {
		this.assessmentType = assessmentType;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getNumberOfViolations() {
		return numberOfViolations;
	}

	public void setNumberOfViolations(Integer numberOfViolations) {
		this.numberOfViolations = numberOfViolations;
	}


}
