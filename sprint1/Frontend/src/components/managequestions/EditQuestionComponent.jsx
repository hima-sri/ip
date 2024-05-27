import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios';
 
 
 
const EditQuestionComponent = () => {
    const location = useLocation();
    const { question } = location.state;
    const [data, setData] = useState([]);
    const topics=import.meta.env.VITE_SUBJECTS_SERVICE
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
        // const interval = setInterval(() => {
        //     fetchData();
        // }, 6000);
        // return () => clearInterval(interval);
    }, []);
 
    const { questionId, usageCount, ...initialQuestion } = question;
    const [editedQuestion, setEditedQuestion] = useState(initialQuestion);
 
 
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setEditedQuestion(prevState => ({
            ...prevState,
            [name]: value
        }));
    };
 
    let subjects = [];
    let subTopics = {};
 
    if (data) {
        data.forEach(item => {
            const subject = item?.courseName;
 
            if (subject && !subjects.includes(subject)) {
                subjects.push(subject);
            }
 
            if (subject && !subTopics[subject]) {
                subTopics[subject] = [];
            }
            // Iterate over each topic within the 'topics' array
            item?.topics?.forEach(topic => {
                const subTopic = topic?.topicName;
 
                if (subject && subTopic && !subTopics[subject].includes(subTopic)) {
                    subTopics[subject].push(subTopic);
                }
            });
        });
    }
 
    // subject and sub-topic handling
    let [selectedSubject, setSelectedSubject] = useState(editedQuestion.subject);
    let [selectedSubTopic, setSelectedSubTopic] = useState(editedQuestion.subTopic);
 
    const handleSubjectChange = (e) => {
        const newSubject = e.target.value;
        setSelectedSubject(newSubject);
        setEditedQuestion((prevState) => ({
            ...prevState,
            subject: newSubject
        }));
    };
 
 
    const handleSubTopicChange = (e) => {
        const newSubTopic = e.target.value;
        setSelectedSubTopic(newSubTopic);
        setEditedQuestion((prevState) => ({
            ...prevState,
            subTopic: newSubTopic
        }));
    };
 
 
    // options and answers handling
 
    const handleOptionsChange = (key, value) => {
        setEditedQuestion(prevState => ({
            ...prevState,
            options: {
                ...prevState.options,
                [key]: value
            }
        }));
    };
 
 
    const handleAnswerChange = (key) => {
        setEditedQuestion((prevQuestion) => {
            const currentAnswers = prevQuestion.answers;
            const answerIndex = currentAnswers.indexOf(key);
 
            if (prevQuestion.answerType === 'Multi-correct') {
                const updatedAnswers = [...currentAnswers];
 
                if (answerIndex === -1) {
                    updatedAnswers.push(key);
                } else {
                    updatedAnswers.splice(answerIndex, 1);
                }
 
                return {
                    ...prevQuestion,
                    answers: updatedAnswers,
                };
            } else {
                return {
                    ...prevQuestion,
                    answers: [key],
                };
            }
        });
    };
 
    // Adding option
    const optionKeys = Object.keys(editedQuestion.options);
    const handleAddOption = () => {
        setEditedQuestion(prevState => {
            const updatedOptions = { ...prevState.options };
            const lastKey = Object.keys(updatedOptions).pop();
            const newKey = String.fromCharCode(lastKey.charCodeAt(0) + 1);
            updatedOptions[newKey] = "";
            return {
                ...prevState,
                options: updatedOptions
            };
        });
    };
 
    // Delete handling
    const handleDeleteOption = (keyToDelete) => {
        setEditedQuestion(prevState => {
            const updatedOptions = { ...prevState.options };
            delete updatedOptions[keyToDelete];
            // Get the remaining keys after deleting the keyToDelete
            const remainingKeys = Object.keys(updatedOptions);
            // Reassign keys based on the remaining order
            const reorderedOptions = {};
            remainingKeys.forEach((key, index) => {
                reorderedOptions[String.fromCharCode(65 + index)] = updatedOptions[key];
            });
 
            return {
                ...prevState,
                options: reorderedOptions
            };
        });
    };
    const navigate = useNavigate();
    // Update handling
   
    const handleUpdate = async (questionId, editedQuestion) => {
        try {
            const update = import.meta.env.VITE_QUESTION_SERVICE
            const response = await axios.patch(`${update}/questions/update/${questionId}`, editedQuestion);
            navigate('/Questions');
 
        } catch (error) {
            console.error('Error updating question:', error);
        }
    };
 
    const handleBack = () => {
        // Redirect to home page logic
        window.location.href = '/Questions'; // Change the URL according to your routing logic
    };
 
 
    return (
        <div className="flex flex-col min-h-screen">
       
 
        <nav className="flex justify-between items-center mb-8 bg-blue-300 text-white py-4 px-6 rounded-md bg-blue-gray-300 w-full">
                <button
                    className="bg-blue-900 text-white font-semibold py-1 px-2 rounded-md transition duration-300"
                    onClick={handleBack}
                >
                    Back
                </button>
                <h1 className="text-3xl font-semibold text-black flex-grow text-center">Edit Question</h1>
</nav>
 
<div >
    {/* <div className="flex flex-col text-gray-700 mt-2">
        <label className="text-lg text-black-600 mb-1">Subject:</label>
        <div>
            <select
                name="subject"
                value={editedQuestion.subject}
                onChange={handleSubjectChange}
                className="p-2 border border-blue-600 rounded-md focus:outline-none focus:border-black"
                style={{ width: '200px' }} // Adjust the width as needed
            >
                {subjects.map((subject) => (
                    <option key={subject} value={subject}>
                        {subject}
                    </option>
                ))}
            </select>
        </div>
    </div> */}
 
    {/* <div className="flex flex-col text-gray-700 mt-2">
        <label className="text-lg text-black-600 mb-1">Sub-Topic:</label>
        <div>
            <select
                name="subTopic"
                value={editedQuestion.subTopic}
                onChange={(e) => setSelectedSubTopic(e.target.value)}
                className="p-2 border border-blue-600 rounded-md focus:outline-none focus:border-black"
                style={{ width: '200px' }} // Adjust the width as needed
            >
                {subTopics[selectedSubject]?.map((topic) => (
                    <option key={topic} value={topic}>
                        {topic}
                    </option>
                ))}
            </select>
        </div>
    </div> */}
 
{/* <div className="flex justify-start w-full">
  <div className="flex flex-col text-gray-700 mt-2">
    <label className="text-lg text-black-600 mb-1">Question:</label>
    <textarea
      name="title"
      value={editedQuestion.title}
      onChange={handleInputChange}
      className="p-2 border border-blue-600 rounded-md focus:outline-none focus:border-black"
      rows={2} // Adjust the number of rows as needed
      style={{ width: '300px' }} // Adjust the width as needed
    />
  </div>
</div> */}
 
    {/* <label className="flex flex-col text-gray-700 mt-2">
        <h2 className="text-lg text-black-600 mb-1">Question:</h2>
        <input
            type="text"
            name="title"
            value={editedQuestion.title}
            onChange={handleInputChange}
            className="p-2 border border-blue-600 rounded-md focus:outline-none focus:border-black"
        />
    </label> */}
<div className="grid grid-cols-1 md:grid-cols-2 gap-10 mt-2">
  <div className="flex flex-col text-gray-700">
    <label className="text-lg text-black-600 mb-1">Question:</label>
    <textarea
      name="title"
      value={editedQuestion.title}
      onChange={handleInputChange}
      className="p-2 border border-blue-600 rounded-md focus:outline-none focus:border-black"
      rows={7}
      style={{ width: '500px' }}
    />
  </div>
 
  <div className="flex flex-col text-gray-700">
    <h2 className="text-lg text-black-600 mb-1">Options:</h2>
    {editedQuestion.options &&
      Object.entries(editedQuestion.options).map(([key, value], index) => (
        <div key={key} className="flex items-center gap-4 mb-2">
          <p className="font-bold text-black text-1.5xl">{String.fromCharCode(65 + index)}</p>
          <input
            type="text"
            value={value}
            onChange={(e) => handleOptionsChange(key, e.target.value)}
            className="p-2 border border-blue-600 rounded-md flex-1 focus:outline-none focus:border-black"
          />
          <button
            onClick={() => handleDeleteOption(key)}
            className="px-4 py-2 border border-red-500 rounded-md bg-white text-red-500"
          >
            Delete
          </button>
        </div>
      ))}
    {optionKeys.length < 6 && (
      <button
        onClick={handleAddOption}
        className="px-2 py-2 border border-blue-600 rounded-md bg-blue-900 text-white"
      >
        Add Option
      </button>
    )}
  </div>
</div>
 
</div>
<div className="grid grid-cols-1 md:grid-cols-2 gap-10">
  <div className="flex flex-col text-gray-700">
    <label className="text-lg text-black-600 mb-1">Question Type:</label>
    <select
      name="questionType"
      value={editedQuestion.questionType}
      onChange={handleInputChange}
      className="p-2 border border-blue-600 rounded-md focus:outline-none focus:border-black mt-2"
    >
      <option value="Direct">Direct</option>
      <option value="Scenario">Scenario Based</option>
    </select>
 
    <label className="text-lg text-black-600 mb-1 mt-4">Answer Type:</label>
    <select
      name="answerType"
      value={editedQuestion.answerType}
      onChange={handleInputChange}
      className="p-2 border border-blue-600 rounded-md focus:outline-none focus:border-black"
    >
      <option value="Multi">Multi</option>
      <option value="Single">Single</option>
    </select>
 
    <label className="text-lg text-black-600 mb-1 mt-4">Difficulty level:</label>
    <select
      name="level"
      value={editedQuestion.level}
      onChange={handleInputChange}
      className="p-2 border border-blue-600 rounded-md focus:outline-none focus:border-black"
    >
      <option value="Easy">Easy</option>
      <option value="Medium">Medium</option>
      <option value="Hard">Hard</option>
    </select>
  </div>
 
  <div className="flex flex-col text-gray-700">
    <label className="text-lg text-black-600 mb-1">Answers:</label>
    {Object.entries(editedQuestion.options).map(([key], index) => (
      <div key={key} className="flex items-center mt-2">
        {editedQuestion.answerType === 'Multi' ? (
          <input
            type="checkbox"
            checked={editedQuestion.answers.includes(key)}
            onChange={() => handleAnswerChange(key)}
            className="mr-2"
          />
        ) : (
          <input
            type="radio"
            checked={editedQuestion.answers[0] === key}
            onChange={() => handleAnswerChange(key)}
            className="mr-2"
          />
        )}
        <span><strong>{String.fromCharCode(65 + index)}</strong></span>
      </div>
    ))}
  </div>
</div>
 
    <button
    onClick={() => handleUpdate(question.questionId, editedQuestion)}
    className="mt-4 px-2 py-2 border border-blue-900 rounded-md bg-blue-900 text-white"
>
    Update
</button>
</div>
 
 
 
    );
};
 
export default EditQuestionComponent;