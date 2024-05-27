import React from 'react';
const QuestionComponent = ({ question, onEdit, onDelete }) => {
    return (
        <div className="bg-white bg-opacity-90 border border-shadow-black rounded-lg p-4 mb-4 text-left shadow-md">
        <h3 className="text-2xl font-bold text-black mb-2">{question.title}</h3>
    
        <div className="mb-4">
            <p className="font-bold">Options:</p>
            <div className="space-y-2">
                {Object.entries(question.options).map(([key, option]) => (
                    <div key={key} className="flex items-center">
                        <span className="font-bold mr-2">{key.toUpperCase()}.</span>
                        <span>{option}</span>
                    </div>
                ))}
            </div>
        </div>
    
        <div className="mb-4">
            <p className="font-bold">Correct Options:</p>
            <ul className="list-disc list-inside">
                {question.answers.map((answer, index) => (
                    <li key={index}> {answer.toUpperCase()}</li>
                ))}
            </ul>
        </div>
    
        <div className="grid grid-cols-2 gap-4 mb-4">
            <div>
                <p><strong>Subject:</strong> {question.subject}</p>
                <p><strong>Sub-Topic:</strong> {question.subTopic}</p>
            </div>
            <div>
                <p><strong>Difficulty:</strong> {question.level}</p>
                <p><strong>Question Type:</strong> {question.questionType}</p>
                <p><strong>Answer Type:</strong> {question.answerType}</p>
            </div>
        </div>
    
        <div className="flex justify-end">
            <button onClick={() => onEdit(question)} className="px-4 py-2 border bg-blue-900 w-24 text-white hover:bg-blue-900 px-2 py-1 rounded-full mr-2">Edit</button>
            
            <button onClick={() => onDelete(question.questionId)} className="px-4 py-2 border bg-red-800 text-white hover:bg-red-800 px-2 py-1 rounded-full mr-2">Delete</button>
        </div>
    </div>
    ); 
};

export default QuestionComponent;