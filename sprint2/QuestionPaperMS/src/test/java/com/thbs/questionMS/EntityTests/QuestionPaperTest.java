package com.thbs.questionMS.EntityTests;

import com.thbs.questionMS.model.QuestionPaper;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuestionPaperTest {


    @Test
    public void testGettersAndSetters() {
        QuestionPaper questionPaper = new QuestionPaper();

        // Set values
        questionPaper.setQuestionPaperId(1L);
        questionPaper.setAssessmentId(100L);
        questionPaper.setSubject("Mathematics");
        questionPaper.setQuestionIds(Arrays.asList(1L, 2L, 3L));

        Map<String, Integer> difficultyLevelSplit = new HashMap<>();
        difficultyLevelSplit.put("easy", 5);
        difficultyLevelSplit.put("medium", 3);
        difficultyLevelSplit.put("hard", 2);
        questionPaper.setDifficultyLevelSplit(difficultyLevelSplit);

        Map<String, Map<String, List<Long>>> questionsSplitIds = new HashMap<>();
        // Add sample data to questionsSplitIds
        questionPaper.setQuestionsSplitIds(questionsSplitIds);

        Map<String, Map<String, Integer>> questionsSplitCount = new HashMap<>();
        // Add sample data to questionsSplitCount
        questionPaper.setQuestionsSplitCount(questionsSplitCount);

        // Test getters
         assertEquals(1L, questionPaper.getQuestionPaperId());
        assertEquals(100L, questionPaper.getAssessmentId());
        assertEquals("Mathematics", questionPaper.getSubject());
        assertEquals(Arrays.asList(1L, 2L, 3L), questionPaper.getQuestionIds());
        assertEquals(difficultyLevelSplit, questionPaper.getDifficultyLevelSplit());
        assertEquals(questionsSplitIds, questionPaper.getQuestionsSplitIds());
        assertEquals(questionsSplitCount, questionPaper.getQuestionsSplitCount());
    }
}
