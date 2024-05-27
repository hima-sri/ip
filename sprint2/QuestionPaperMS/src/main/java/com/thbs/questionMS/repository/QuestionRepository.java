package com.thbs.questionMS.repository;

import com.thbs.questionMS.model.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QuestionRepository extends MongoRepository<Question,Long> {
    public List<Question> findBySubject(String subject);
    public List<Question> findQuestionsBySubjectOrderByUsageCountAsc(String subject);
    @Query("{'subject': ?0, 'subTopic': ?1, 'level': ?2}, {'questionId': 1}")
//    List<Long> findQuestionIdsBySubjectAndSubTopicAndLevel(String subject, String subTopic, String level);
    List<Question> findBySubjectAndSubtopicAndLevel(String subject, String subtopic, String level);

    List<Question> getQuestionsBySubject(String subject);
}