
import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const ScheduleAssessmentComponent = () => {
  const [assessmentName, setAssessmentName] = useState("");
  const [batchNames, setBatchNames] = useState([]);
  const [selectedBatch, setSelectedBatch] = useState("");
  const [startTime, setStartTime] = useState("");
  const [endTime, setEndTime] = useState("");
  const [durationMinutes, setDurationMinutes] = useState("");
  const [numberOfQuestions, setNumberOfQuestions] = useState("");
  const [passingPercentage, setPassingPercentage] = useState("");
  const [status, setStatus] = useState(true);
  const [numberOfViolations, setNumberOfViolations] = useState(0);
  const [courseName, setCourseName] = useState("");
  const [assessmentType, setAssessmentType] = useState("Regular Assessment");
  const [errorMessage, setErrorMessage] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchBatchNames = async () => {
      try {
        const baseUrl = import.meta.env.VITE_BATCH_URL;
        const response = await axios.get(`${baseUrl}/batch/name`);
        const batchNames = response.data;
        setBatchNames(batchNames);
      } catch (error) {
        console.error("Error fetching batch names: ", error);
      }
    };
    fetchBatchNames();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrorMessage("");
    setSuccessMessage("");
    setSubmitting(true);

    try {
      const baseUrl = import.meta.env.VITE_SCHEDULE_ASS_URL;
      console.log("Selected Batch:", selectedBatch);
      await axios.post(`${baseUrl}/assessments`, {
        assessmentName,
        targetBatch: selectedBatch,
        startTime,
        endTime,
        durationMinutes,
        numberOfQuestions,
        passingPercentage,
        status,
        numberOfViolations,
        courseName,
        assessmentType,
      });
      setSuccessMessage("Assessment scheduled successfully!");
      window.alert("Assessment scheduled successfully!");
      navigate("/assessment");
    } catch (error) {
      setErrorMessage("Failed to schedule assessment. Please try again.");
      console.error("Error scheduling assessment: ", error);
    } finally {
      setSubmitting(false);
    }
  };

  const isFutureDate = (dateString) => {
    const selectedDate = new Date(dateString);
    const currentDate = new Date();
    return selectedDate > currentDate;
  };

  const isPositiveNumber = (value) => {
    return value >= 0 && /^\d+$/.test(value);
  };

  return (
    <div className="text-black bg-white ">
      <div className="p-5">
       
        <h1 className="text-blue-900 text-4xl font-bold mt-3 mb-4">
          SCHEDULE NEW ASSESSMENT
        </h1>
        <form onSubmit={handleSubmit} className="space-y-4 text-left ml-60 mt-12">
          <div>
            <label htmlFor="assessmentName" className="text-black">
              Assessment Name:
            </label>
            <input
              type="text"
              id="assessmentName"
              value={assessmentName}
              onChange={(e) => setAssessmentName(e.target.value)}
              className="text-center bg-gray-100 px-2 py-2 shadow-lg rounded-full text-black ml-24 w-1/2 focus:border-transparent focus:outline-none"
              required
            />
          </div>
          <div>
            <label htmlFor="courseName" className="text-black">
              Course name :
            </label>
            <input
              type="text"
              id="courseName"
              value={courseName}
              onChange={(e) => setCourseName(e.target.value)}
              className="text-center bg-gray-100 px-2 py-2 shadow-lg rounded-full text-black ml-32 w-1/2 focus:border-transparent focus:outline-none"
              required
            />
          </div>
          <div>
            <label htmlFor="assessmentType" className="text-black">
              Assessment Type:
            </label>
            <select
              id="assessmentType"
              value={assessmentType}
              onChange={(e) => setAssessmentType(e.target.value)}
              className="text-center bg-gray-100 px-2 py-2 shadow-lg rounded-full text-black ml-28 w-1/2 focus:border-transparent focus:outline-none"
              required
            >
              <option value="Regular Assessment">Regular Assessment</option>
              <option value="Re-assessment">Re-assessment</option>
            </select>
          </div>
          <div>
            <label htmlFor="targetBatch" className="text-black">
              Select Batch:
            </label>
            <select
              id="targetBatch"
              value={selectedBatch}
              onChange={(e) => setSelectedBatch(e.target.value)}
              className="text-center bg-gray-100 px-2 py-2 shadow-lg rounded-full text-black ml-36 w-1/2 focus:border-transparent focus:outline-none"
              required
            >
              <option value="">Select Batch</option> 
              {batchNames.map((batchName) => (
                <option key={batchName} value={batchName}>
                  {batchName}
                </option>
              ))}
            </select>
          </div>
          <div>
            <label htmlFor="startTime" className="text-black">
              Start Time:
            </label>
            <input
              type="datetime-local"
              id="startTime"
              value={startTime}
              min={new Date().toISOString().slice(0, 16)}
              onChange={(e) => setStartTime(e.target.value)}
              className="text-center bg-gray-100 px-2 py-2 shadow-lg rounded-full text-black ml-40 w-1/2 focus:border-transparent focus:outline-none"
              required
            />
          </div>
          <div>
            <label htmlFor="endTime" className="text-black">
              End Time:
            </label>
            <input
              type="datetime-local"
              id="endTime"
              value={endTime}
              min={startTime}
              onChange={(e) => setEndTime(e.target.value)}
              className="text-center bg-gray-100 px-2 py-2 shadow-lg rounded-full text-black ml-40 w-1/2 focus:border-transparent focus:outline-none"
              required
            />
          </div>
          <div>
            <label htmlFor="durationMinutes" className="text-black">
              Duration (in minutes):
            </label>
            <input
              type="number"
              id="durationMinutes"
              value={durationMinutes}
              onChange={(e) =>
                setDurationMinutes(
                  isPositiveNumber(e.target.value) ? e.target.value : ""
                )
              }
              className="text-center bg-gray-100 px-2 py-2 shadow-lg rounded-full text-black ml-20 w-1/2 focus:border-transparent focus:outline-none"
              required
            />
          </div>
          <div>
            <label htmlFor="numberOfQuestions" className="text-black">
              Number of Questions:
            </label>
            <input
              type="number"
              id="numberOfQuestions"
              value={numberOfQuestions}
              onChange={(e) =>
                setNumberOfQuestions(
                  isPositiveNumber(e.target.value) ? e.target.value : ""
                )
              }
              className="text-center bg-gray-100 px-2 py-2 shadow-lg rounded-full text-black ml-20 w-1/2 focus:border-transparent focus:outline-none"
              required
            />
          </div>
          <div>
            <label htmlFor="passingPercentage" className="text-black">
              Passing Percentage:
            </label>
            <input
              type="number"
              id="passingPercentage"
              value={passingPercentage}
              onChange={(e) =>
                setPassingPercentage(
                  isPositiveNumber(e.target.value) ? e.target.value : ""
                )
              }
              className="text-center bg-gray-100 px-2 py-2 shadow-lg rounded-full text-black ml-24 w-1/2 focus:border-transparent focus:outline-none"
              required
            />
          </div>
          <div>
            <label htmlFor="numberOfViolations" className="text-black">
              Number of Violations Allowed:
            </label>
            <input
              type="number"
              id="numberOfViolations"
              value={numberOfViolations}
              onChange={(e) =>
                setNumberOfViolations(
                  isPositiveNumber(e.target.value) ? e.target.value : ""
                )
              }
              className="text-center bg-gray-100 px-2 py-2 shadow-lg rounded-full text-black ml-6 w-1/2 focus:border-transparent focus:outline-none"
              required
            />
          </div>
          <div>
            <button
              type="submit"
              className="bg-blue-900 text-white px-4 py-2 ml-80 mt-6 rounded-full transition-colors"
              disabled={submitting}
            >
              {submitting ? "Submitting..." : "Submit Assessment "}
            </button>
          </div>
          {errorMessage && (
            <div className="text-red-800 text-center">{errorMessage}</div>
          )}
          {successMessage && (
            <div className="text-green-600 text-center">{successMessage}</div>
          )}
        </form>
      </div>
    </div>
  );
};

export default ScheduleAssessmentComponent;
