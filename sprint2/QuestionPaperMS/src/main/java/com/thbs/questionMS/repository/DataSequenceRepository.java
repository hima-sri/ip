package com.thbs.questionMS.repository;

import com.thbs.questionMS.model.DataSequences;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DataSequenceRepository extends MongoRepository<DataSequences,String> {
}
