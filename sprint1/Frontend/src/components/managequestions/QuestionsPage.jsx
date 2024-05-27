import React, { useState, useEffect } from 'react';
import Filter from './Filter';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import QuestionComponent from './QuestionComponent';

const QuestionsPage = () => {
  const [filteredQuestions, setFilteredQuestions] = useState([]);
  const [questionsData, setQuestionsData] = useState([]);
  const navigate = useNavigate();

  const handleEdit = (question) => {
    navigate('/EditQuestion', { state: { question } });
  };

  const fetchQuestionsData = () => {
    const questions = import.meta.env.VITE_QUESTION_SERVICE;
    axios.get(`${questions}/questions/all`)
      .then(response => {
        setQuestionsData(response.data);
      })
      .catch(error => {
        console.error('Error fetching data:', error);
      });
  };

  useEffect(() => {
    fetchQuestionsData();
  }, []);

  const handleFilter = (filters) => {
    let filteredData = questionsData;
    if (filters.subject) {
      const subjectFilter = filters.subject.toLowerCase();
      filteredData = filteredData.filter(question => question.subject.toLowerCase() === subjectFilter);
    }
    
    if (filters.subTopic) {
      const subTopicFilter = filters.subTopic.toLowerCase();
      filteredData = filteredData.filter(question => question.subTopic.toLowerCase() === subTopicFilter);
    }
    
    if (filters.level) {
      const levelFilter = filters.level.toLowerCase();
      filteredData = filteredData.filter(question => question.level.toLowerCase() === levelFilter);
    }
    
    if (filters.questionType) {
      const questionTypeFilter = filters.questionType.toLowerCase();
      filteredData = filteredData.filter(question => question.questionType.toLowerCase() === questionTypeFilter);
    }
    
    if (filters.answerType) {
      const answerTypeFilter = filters.answerType.toLowerCase();
      filteredData = filteredData.filter(question => question.answerType.toLowerCase() === answerTypeFilter);
    }
    
    setFilteredQuestions(filteredData.length === questionsData.length ? [] : filteredData);
  };

  const handleDelete = (questionId) => {
    const deleteurl = import.meta.env.VITE_QUESTION_SERVICE;
    axios.delete(`${deleteurl}/questions/delete/${questionId}`)
      .then(response => {
        alert('Question deleted successfully');
      })
      .catch(error => {
        console.error('Error deleting question:', error);
      });

    setFilteredQuestions(prevQuestions => (
      prevQuestions.filter(question => question.questionId !== questionId)
    ));
    setQuestionsData(prevQuestions => (
      prevQuestions.filter(question => question.questionId !== questionId)
    ));
  };

  return (
    <div className="font-sans p-5 flex flex-col justify-center items-center bg-white min-h-screen">
      <div className="w-full max-w-4xl">
        <Filter onFilter={handleFilter} />
        <div className="mt-12">
          <h2 className="text-lg text-blue-900 font-bold uppercase text-center mb-4">Filtered Questions</h2>
          {filteredQuestions.length === 0 ? (
            <p className="text-red-500 text-center">No questions found</p>
          ) : (
            <div>
              <p className="text-green-500 text-center mb-4">Found {filteredQuestions.length} questions</p>
              <br></br>
              <br></br>
              <br></br>
              <br></br>
              <br></br>
              <br></br>
              <br></br>
              <ol className="list-decimal list-inside">
                {filteredQuestions.map(question => (
                  <QuestionComponent key={question.questionId} question={question} onEdit={handleEdit} onDelete={handleDelete} />
                ))}
              </ol>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default QuestionsPage;
