
import React from "react";

const QuestionLayout = (props) => {
  return (
    
    
    <div className="question-cont h-auto m-10 w-auto  flex flex-col items-center justify-center w-3/4 shadow-md m-4 rounded-lg border border-gray-300 bg-gray-100">
      <div className="subject mt-2 mb-2">Subject: {props.subject}</div>
      <div className="question">
        Q{props.qnum + 1}. {props.question}
      </div>

      <div className="mt-10 mb-5 w-80 h-auto mx-auto rounded-lg">
        <div className= {` rounded-lg m-0.5 option ${props.answer.includes('A') ? 'bg-green-600 text-white' : ''} `}>
          <span className="mr-1">a. {props.option1}</span>
        </div>

        {props.option2 && props.option2.length > 0 && (
          <div className={` rounded-lg m-0.5 option ${props.answer.includes('B') ? 'bg-green-600 text-white' : ''}`}>
            <span className="mr-1">b. {props.option2}</span>
          </div>
        )}

        {props.option3 && props.option3.length > 0 && (
          <div className={` rounded-lg m-0.5 option ${props.answer.includes('C') ? 'bg-green-600 text-white' : ''}`}>
            <span className="mr-1">c. {props.option3}</span>
          </div>
        )}

        {props.option4 && props.option4.length > 0 && (
          <div className={` rounded-lg m-0.5 option ${props.answer.includes('D') ? 'bg-green-600 text-white' : ''}`}>
            <span className="mr-1">d. {props.option4}</span>
          </div>
        )}

        {props.option5 && props.option5.length > 0 && (
          <div className={` rounded-lg m-0.5 option ${props.answer.includes('E') ? 'bg-green-600 text-white' : ''}`}>
            <span className="mr-1">e. {props.option5}</span>
          </div>
        )}

        {props.option6 && props.option6.length > 0 && (
          <div className={` rounded-lg m-0.5 option ${props.answer.includes('F') ? 'bg-green-600 text-white' : ''}`}>
            <span className="mr-1">f. {props.option6}</span>
          </div>
        )}

        {props.option7 && props.option7.length > 0 && (
          <div className={` rounded-lg m-0.5 option ${props.answer.includes('G') ? 'bg-green-600 text-white' : ''}`}>
            <span className="mr-1">g. {props.option7}</span>
          </div>
        )}

        {props.option8 && props.option8.length > 0 && (
          <div className={` rounded-lg m-0.5 option ${props.answer.includes('H') ? 'bg-green-600 text-white' : ''}`}>
            <span className="mr-1">h. {props.option8}</span>
          </div>
        )}
      </div>

      <div className="mt-10  mb-4 mx-auto max-w-6xl">
        <div className="w-90  flex justify-between space-x-80">
          <div className="sub-topic">Sub Topic: {props.subTopic}</div>
          <div className="difficulty-level">Difficulty Level: {props.level}</div>
        </div>

        <div className="mt-5 w-90 mb-2 flex justify-between space-x-80">
          <div className="q-type">Type: {props.qtype}</div>
          <div className="a-type">Answer Type: {props.atype}</div>
        </div>
      </div>
    </div>
  );
};

export default QuestionLayout;



