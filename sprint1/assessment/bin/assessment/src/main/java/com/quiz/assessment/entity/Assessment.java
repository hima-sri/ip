package com.quiz.assessment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(name = "status", columnDefinition = "boolean default true")
    private Boolean status = true;

    @Column(name = "number_of_voilations")
    private Integer numberOfVoilations;


    public Long getId() {
        return id;
    }
//    @Override
//    public String toString() {
//        return "Assessment{" +
//                "id=" + id +
//                ", assessmentName='" + assessmentName + '\'' +
//                ", targetBatch='" + targetBatch + '\'' +
//                ", startTime=" + startTime +
//                ", endTime=" + endTime +
//                ", durationMinutes=" + durationMinutes +
//                ", numberOfQuestions=" + numberOfQuestions +
//                ", passingPercentage=" + passingPercentage +
//                ", isActive=" + isActive +
//                ", status=" + status +
//                '}';
//    }
}
