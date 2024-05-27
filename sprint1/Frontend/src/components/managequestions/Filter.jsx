import React, { useState, useEffect } from 'react';
import axios from 'axios';
 
const Filter = ({ onFilter }) => {
  const topics = import.meta.env.VITE_SUBJECTS_SERVICE;
  const [data, setData] = useState([]);
  const [subjects, setSubjects] = useState([]);
  const [subTopics, setSubTopics] = useState({});
  const [selectedSubject, setSelectedSubject] = useState('');
  const [selectedSubTopic, setSelectedSubTopic] = useState('');
  const [selectedLevel, setSelectedLevel] = useState('');
  const [selectedQuestionType, setSelectedQuestionType] = useState('');
  const [selectedAnswerType, setSelectedAnswerType] = useState('');
 
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get(`${topics}/course/dto`);
        setData(response.data);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };
    fetchData();
    const interval = setInterval(fetchData, 6000);
    return () => clearInterval(interval);
  }, []);
 
  useEffect(() => {
    if (data.length > 0) {
      const subjects = data.map(item => item?.courseName).filter(Boolean);
      setSubjects(subjects);
 
      const subTopicsMap = {};
      data.forEach(item => {
        const subject = item?.courseName;
        if (subject) {
          subTopicsMap[subject] = item.topics.map(topic => topic?.topicName).filter(Boolean);
        }
      });
      setSubTopics(subTopicsMap);
    }
  }, [data]);
 
  const difficulties = ['Easy', 'Medium', 'Hard'];
  const questionTypes = ['Direct', 'Scenario Based'];
  const answerTypes = ['Multi', 'Single'];
 
  const handleFilter = () => {
    const filters = {
      subject: selectedSubject,
      subTopic: selectedSubTopic,
      level: selectedLevel,
      questionType: selectedQuestionType,
      answerType: selectedAnswerType,
    };
    onFilter(filters);
  };
 
  const handleBack = () => {
    window.location.href = '/'; // Change the URL according to your routing logic
  };
 
  return (
    
    <div>
      
      <nav className=" top-0 left-0 w-full flex justify-between items-center mb-8 bg-blue-300 text-white py-4 px-6 rounded-md bg-blue-gray-300">
        <button
          className="bg-blue-900 text-white font-semibold py-1 px-2 rounded-md transition duration-300"
          onClick={handleBack}
        >
          Back
        </button>
        <h1 className="text-3xl font-semibold text-black flex-grow text-center">Manage Questions</h1>
      </nav>
      <div className="mt-20 px-4">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-10 mb-4">
          <label className="flex flex-col mb-4">
            Subject:
            <select
              className="w-full py-2 px-3 border border-gray-300 rounded-md focus:outline-none focus:border-black"
              value={selectedSubject}
              onChange={(e) => setSelectedSubject(e.target.value)}
              style={{ width: '250px' }}
            >
              <option value="">Select Subject</option>
              {subjects.map((subject) => (
                <option key={subject} value={subject}>
                  {subject}
                </option>
              ))}
            </select>
          </label>
 
          <label className="flex flex-col mb-4">
            Sub-Topic:
            <select
              className="w-full py-2 px-3 border border-gray-300 rounded-md focus:outline-none focus:border-black"
              value={selectedSubTopic}
              onChange={(e) => setSelectedSubTopic(e.target.value)}
              style={{ width: '250px', overflowX: 'auto' }}
            >
              <option value="">Select Sub-Topic</option>
              {subTopics[selectedSubject]?.map((topic) => (
                <option key={topic} value={topic}>{topic}</option>
              ))}
            </select>
          </label>
 
          <label className="flex flex-col mb-4">
            Difficulty:
            <select
              className="w-full py-2 px-3 border border-gray-300 rounded-md focus:outline-none focus:border-black"
              value={selectedLevel}
              onChange={(e) => setSelectedLevel(e.target.value)}
              style={{ width: '250px' }}
            >
              <option value="">Select Difficulty Level</option>
              {difficulties.map((difficulty) => (
                <option key={difficulty} value={difficulty}>{difficulty}</option>
              ))}
            </select>
          </label>
        </div>
 
        <div className="grid grid-cols-1 md:grid-cols-2 gap-10 mb-4">
          <label className="flex flex-col mb-4 items-center">
            Question Type:
            <select
              className="w-full py-2 px-3 border border-gray-300 rounded-md focus:outline-none focus:border-black"
              value={selectedQuestionType}
              onChange={(e) => setSelectedQuestionType(e.target.value)}
              style={{ width: '250px' }}
            >
              <option value="">Select Question Type</option>
              {questionTypes.map((type) => (
                <option key={type} value={type}>{type}</option>
              ))}
            </select>
          </label>
 
          <label className="flex flex-col items-center mb-4">
            Answer Type:
            <select
              className="w-full py-2 px-3 border border-gray-300 rounded-md focus:outline-none focus:border-black"
              value={selectedAnswerType}
              onChange={(e) => setSelectedAnswerType(e.target.value)}
              style={{ width: '250px' }}
            >
              <option value="">Select Answer Type</option>
              {answerTypes.map((type) => (
                <option key={type} value={type}>{type}</option>
              ))}
            </select>
          </label>
        </div>
        <div className="flex justify-center">
          <button
            className="py-2 px-4 bg-blue-900 text-white rounded-md focus:outline-none"
            onClick={handleFilter}
          >
            Filter
          </button>
        </div>
      </div>
    </div>
  );
};
 
export default Filter;