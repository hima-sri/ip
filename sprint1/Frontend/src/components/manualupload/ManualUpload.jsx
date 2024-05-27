import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const difficulties = ['Easy', 'Medium', 'Hard'];
const optionLetters = ['A', 'B', 'C', 'D', 'E', 'F'];

const ManualUpload = () => {
    const [selectedCourse, setSelectedCourse] = useState('');
    const [selectedSubtopic, setSelectedSubtopic] = useState('');
    const [selectedDifficulty, setSelectedDifficulty] = useState('');
    const [selectedQuestionType, setSelectedQuestionType] = useState('');
    const [question, setQuestion] = useState('');
    const [answerType, setAnswerType] = useState('Single'); // Default to Single
    const [numOptions, setNumOptions] = useState(2); // Default to 2 options
    const [options, setOptions] = useState(Array(numOptions).fill(''));
    const [correctOptions, setCorrectOptions] = useState([]);
    const [courses, setCourses] = useState([]);
    const [subtopics, setSubtopics] = useState([]);
    const topics = import.meta.env.VITE_SUBJECTS_SERVICE;
    
    useEffect(() => {
        // Fetch data from the API endpoint
        axios.get(`${topics}/course/dto`)
            .then(response => {
                const data = response.data;
                if (Array.isArray(data) && data.length > 0) {
                    setCourses(data);
                } else {
                    console.error('Data is not in the expected format');
                }
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    }, []);

    const fetchSubtopics = (courseName) => {
        const selectedCourse = courses.find(course => course.courseName === courseName);
        if (selectedCourse) {
            setSubtopics(selectedCourse.topics.map(topic => topic.topicName));
        }
    };

    const handleAddQuestion = (event) => {
        // Prevent the default form submission behavior
        event.preventDefault();

        // Validate form
        if (!selectedCourse || !selectedSubtopic || !selectedDifficulty || !selectedQuestionType || !question || !answerType || options.some(opt => !opt) || correctOptions.length === 0) {
            toast.error('Please fill all the fields!');
            return;
        }

        // Construct options as key-value pairs
        const formattedOptions = {};
        options.forEach((option, index) => {
            formattedOptions[optionLetters[index]] = option;
        });

        // Prepare the question object
        const questionData = {
            subject: selectedCourse,
            subTopic: selectedSubtopic,
            level: selectedDifficulty,
            questionType: selectedQuestionType,
            title: question,
            answerType,
            options: formattedOptions,
            answers: correctOptions,
        };
        const manualurl = import.meta.env.VITE_QUESTION_SERVICE;
        // Send POST request to the backend
        axios.post(`${manualurl}/questions/add`, questionData)
            .then(response => {
                // Reset form fields after adding question
                setSelectedCourse('');
                setSelectedSubtopic('');
                setSelectedDifficulty('');
                setSelectedQuestionType('');
                setQuestion('');
                setAnswerType('Single'); // Reset answer type to Single
                setNumOptions(2); // Reset number of options to 2
                setOptions(Array(2).fill(''));
                setCorrectOptions([]);
                // Show success message
                toast.success('Question uploaded successfully');
            })
            .catch(error => {
                console.error('Error adding question:', error);
                toast.error('Failed to add question. Please try again.');
            });
    };

    const handleCourseChange = (event) => {
        const courseName = event.target.value;
        setSelectedCourse(courseName);
        fetchSubtopics(courseName);
    };

    const handleNumOptionsChange = (event) => {
        const num = parseInt(event.target.value);
        setNumOptions(num);
        setOptions(Array(num).fill(''));
    };

    const handleOptionChange = (index, value) => {
        const newOptions = [...options];
        newOptions[index] = value;
        setOptions(newOptions);
    };

    const handleCorrectOptionChange = (index, isChecked) => {
        const optionLetter = optionLetters[index];
        if (isChecked) {
            setCorrectOptions([...correctOptions, optionLetter]);
        } else {
            const updatedCorrectOptions = correctOptions.filter(opt => opt !== optionLetter);
            setCorrectOptions(updatedCorrectOptions);
        }
    };

    const handleBack = () => {
        // Redirect to home page logic
        window.location.href = '/'; // Change the URL according to your routing logic
    };
    return (
        <div className="flex flex-col justify-center items-center max-w-full min-h-screen overflow-hidden">
        
      
            
            <nav className="flex justify-between items-center mb-8 bg-blue-300 text-white py-4 px-6 rounded-md bg-blue-gray-300 w-full">
    <button
        className="bg-blue-900 text-white font-semibold py-1 px-2 rounded-md transition duration-300"
        onClick={handleBack}
    >
        Back
    </button>
    <h1 className="text-3xl font-semibold text-black flex-grow text-center">Manual Upload</h1>
   
</nav>
            <form className="space-y-4 w-full max-w-screen-xl px-8">
                
            {/* {errorMessage && (
            <div className="error-alert bg-red-500 text-white py-2 px-4 absolute bottom-20 right-4 z-20 rounded-md">
                {errorMessage}
            </div>
        )} */}
                <div className="flex flex-wrap">
                    <div className="w-full md:w-1/3 mb-4 md:mb-0 md:pr-2">
                        <div className="flex flex-col">
                            <label htmlFor="course" className="text-gray-700 font-medium text-left">Course:</label>
                            <select
                                id="course"
                                value={selectedCourse}
                                onChange={handleCourseChange}
                                className="w-full py-2 px-3 border border-gray-300 rounded-md focus:outline-none focus:border-black"
                            >
                                <option value="">Select a Course</option>
                                {courses.map(course => (
                                    <option key={course.courseId} value={course.courseName}>{course.courseName}</option>
                                ))}
                            </select>
                        </div>
                    </div>
                    <div className="w-full md:w-1/3 mb-4 md:mb-0 md:px-2">
                        <div className="flex flex-col">
                            <label htmlFor="subtopic" className="text-gray-700 font-medium text-left">Subtopic:</label>
                            <select
                                id="subtopic"
                                value={selectedSubtopic}
                                onChange={(e) => setSelectedSubtopic(e.target.value)}
                                className="w-full py-2 px-3 border border-gray-300 rounded-md focus:outline-none focus:border-black"
                            >
                                <option value="">Select a Subtopic</option>
                                {subtopics.map((subtopic, index) => (
                                    <option key={index} value={subtopic}>{subtopic}</option>
                                ))}
                            </select>
                        </div>
                    </div>
                    <div className="w-full md:w-1/3 md:pl-2">
                        <div className="flex flex-col">
                            <label htmlFor="difficulty" className="text-gray-700 font-medium text-left">Difficulty:</label>
                            <select
                                id="difficulty"
                                value={selectedDifficulty}
                                onChange={(e) => setSelectedDifficulty(e.target.value)}
                                required
                                className="w-full py-2 px-3 border border-gray-300 rounded-md focus:outline-none focus:border-black"
                            >
                                <option value="">Select Difficulty</option>
                                {difficulties.map(difficulty => (
                                    <option key={difficulty} value={difficulty}>{difficulty}</option>
                                ))}
                            </select>
                        </div>
                    </div>
                </div>

                <div className="flex flex-col">
                    <label className="text-gray-700 font-medium text-left">Question Type:</label>
                    <div className="flex items-center mt-2">
                        <label className="flex items-center mr-4">
                            <input
                                type="radio"
                                value="Direct"
                                checked={selectedQuestionType === 'Direct'}
                                onChange={(e) => setSelectedQuestionType(e.target.value)}
                                required
                                className="mr-2"
                            />
                            <span className="text-gray-900">Direct</span>
                        </label>
                        <label className="flex items-center">
                            <input
                                type="radio"
                                value="Scenario"
                                checked={selectedQuestionType === 'Scenario'}
                                onChange={(e) => setSelectedQuestionType(e.target.value)}
                                required
                                className="mr-2"
                            />
                            <span className="text-gray-900">Scenario based</span>
                        </label>
                    </div>
                </div>

                <div className="flex flex-col">
                    <label htmlFor="question" className="text-gray-700 font-medium text-left">Question:</label>
                    <textarea
                        id="question"
                        value={question}
                        onChange={(e) => setQuestion(e.target.value)}
                        className="w-full py-2 px-3 border border-gray-300 rounded-md focus:outline-none focus:border-black"
                        required
                        placeholder="Enter question here"
                        rows={4} // Set the number of rows you want
                    />
                </div>

                <div className="flex flex-col">
                    <label className="text-gray-700 font-medium text-left">Answer Type:</label>
                    <select
                        value={answerType}
                        onChange={(e) => setAnswerType(e.target.value)}
                        required
                        className="w-full py-2 px-3 border border-gray-300 rounded-md focus:outline-none focus:border-black"
                    >
                        <option value="Single">Single</option>
                        <option value="Multi">Multi</option>
                    </select>
                </div>

                <div className="flex flex-col">
                    <label htmlFor="numOptions" className="text-gray-700 font-medium text-left">Number of Options:</label>
                    <select
                        id="numOptions"
                        value={numOptions}
                        onChange={handleNumOptionsChange}
                        required
                        className="w-full py-2 px-3 border border-gray-300 rounded-md focus:outline-none focus:border-black"
                    >
                        {[2, 3, 4, 5, 6].map(num => (
                            <option key={num} value={num}>{num}</option>
                        ))}
                    </select>
                </div>

                {options.map((option, index) => (
                    <div key={index} className="flex flex-col">
                        <label htmlFor={`option${index}`} className="text-gray-700 font-medium text-left">{optionLetters[index]}:</label>
                        <input
                            type="text"
                            id={`option${index}`}
                            value={option}
                            onChange={(e) => handleOptionChange(index, e.target.value)}
                            className="w-full py-2 px-3 border border-gray-300 rounded-md focus:outline-none focus:border-black"
                            required
                            placeholder={`Enter option ${optionLetters[index]}`}
                        />
                    </div>
                ))}

                <div className="flex flex-col">
                    <label className="text-gray-700 font-medium text-left">Correct Options:</label>
                    {options.map((_, index) => (
                        <label key={index} className="text-gray-700 flex items-center mt-2">
                            {answerType === 'Single' ? (
                                <input
                                    type="radio"
                                    name="correctOption"
                                    checked={correctOptions.includes(optionLetters[index])}
                                    onChange={(e) => handleCorrectOptionChange(index, e.target.checked)}
                                />
                            ) : (
                                <input
                                    type="checkbox"
                                    checked={correctOptions.includes(optionLetters[index])}
                                    onChange={(e) => handleCorrectOptionChange(index, e.target.checked)}
                                />
                            )}
                            <span className="ml-2">{optionLetters[index]}</span>
                        </label>
                    ))}
                </div>

                <button
                    type="submit"
                    onClick={handleAddQuestion}
                    className="bg-blue-900 text-white font-semibold py-2 px-4 rounded-md transition duration-300 mt-4"
                >
                    Add Question
                </button>

                {/* <ToastContainer position="bottom-right" autoClose={3000} /> */}

            </form>
            <ToastContainer position="bottom-right" autoClose={3000} />
        </div>
    );
};

export default ManualUpload;
