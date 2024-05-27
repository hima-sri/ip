package com.thbs.questionMS.model;



import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//imports
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "questions")
public class Question {


  @Transient
  public static final String SEQUENCE_NAME = "questions_sequence";

  @Id
  private Long questionId;
  private String subject;
  private String subTopic;
  private String title;
  private Map<String, String> options;
  private String level;
  private String questionType;
  private String answerType;
  private List<String> answers;

  private Integer usageCount = 0 ;

  public Question(String subject, String subTopic,
                  String title,  Map<String, String> options, String level,
                  String type, String answerType, List<String> answers) {
      this.subject = subject;
      this.subTopic = subTopic;
      this.title = title;
      this.options = options;
      this.level = level;
      this.questionType = type;
      this.answerType = answerType;
      this.answers = answers;
  }

  public Question() {
      this.options = new LinkedHashMap<>();
  }
  public Long getQuestionId() {
      return questionId;
  }

  public void setQuestionId(Long id) {
      this.questionId = id;
  }

  public String getSubject() {
      return subject;
  }

  public void setSubject(String subject) {
      this.subject = subject;
  }

  public String getSubTopic() {
      return subTopic;
  }

  public void setSubTopic(String subTopic) {
      this.subTopic = subTopic;
  }

  public String getTitle() {
      return title;
  }

  public void setTitle(String title) {
      this.title = title;
  }

  public Map<String, String> getOptions() {
      return options;
  }

  public void setOptions(Map<String, String> map) {
      this.options =  map;
  }

  public String getLevel() {
      return level;
  }

  public void setLevel(String level) {
      this.level = level;
  }

  public String getQuestionType() {
      return questionType;
  }

  public void setQuestionType(String type) {
      this.questionType = type;
  }

  public String getAnswerType() {
      return answerType;
  }

  public void setAnswerType(String answerType) {
      this.answerType = answerType;
  }

  public List<String> getAnswers() {
      return answers;
  }

  public void setAnswers(List<String> answers) {
      this.answers = answers;
  }

 

  public Integer getUsageCount() {
		return usageCount;
	}

	public void setUsageCount(Integer usageCount) {
		this.usageCount = usageCount;
	}

	@Override
  public String toString() {
      return "Question{" +
              "id=" + questionId +
              ", subject='" + subject + '\'' +
              ", subTopic='" + subTopic + '\'' +
              ", title='" + title + '\'' +
              ", options=" + options +
              ", level='" + level + '\'' +
              ", type='" + questionType + '\'' +
              ", answerType='" + answerType + '\'' +
              ", answers=" + answers +
              '}';
  }

}