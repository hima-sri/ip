package com.quiz.assessment.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.quiz.assessment.entity.Assessment;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment,Long> {


    Optional<Assessment> findAssessmentByAssessmentName(String assessmentName);

    @Query("SELECT a FROM Assessment a WHERE a.startTime <= :currentTime AND a.endTime >= :currentTime AND a.status IS TRUE")
    List<Assessment> findActiveAssessments(@Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT a FROM Assessment a WHERE a.startTime > :currentTime AND a.status IS TRUE")
    List<Assessment> findUpcomingAssessments(@Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT a FROM Assessment a WHERE a.startTime < :currentTime AND a.endTime < :currentTime OR a.status IS FALSE ORDER BY a.id DESC")
    List<Assessment> findCompletedAssessments(@Param("currentTime") LocalDateTime currentTime);


    // Repository layer
    @Query("SELECT DISTINCT a.courseName FROM Assessment a WHERE a.targetBatch = :targetbatch")
    List<String> getTechnologyByBatch(@Param("targetbatch") String targetbatch);
    @Query("SELECT a FROM Assessment a ORDER BY a.id DESC")
    List<Assessment> findAssessments();
}
