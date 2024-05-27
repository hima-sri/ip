package com.thbs.questionMS.repository;

import com.thbs.questionMS.model.QuestionPaper;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionPaperRepository extends MongoRepository<QuestionPaper, Long> {

    Optional<QuestionPaper> findByAssessmentId(Long assessmentId);

    boolean existsByAssessmentId(Long assessmentId);
}
