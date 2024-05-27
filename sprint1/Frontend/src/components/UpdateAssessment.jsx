
import React, { useState, useEffect } from "react";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom";

const UpdateAssessment = () => {
  const { id } = useParams();
  const [assessment, setAssessment] = useState({
    id: "",
    assessmentName: "",
    targetBatch: "",
    startTime: "",
    endTime: "",
    durationMinutes: "",
    numberOfQuestions: "",
    numberOfViolations: "",
    passingPercentage: "",
    courseName: "", // Changed from 'technology'
  });
  const [batchNames, setBatchNames] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchAssessment = async () => {
      try {
        const baseUrl = import.meta.env.VITE_SCHEDULE_ASS_URL;
        const response = await axios.get(`${baseUrl}/assessments/id/${id}`);
        setAssessment(response.data);
        setLoading(false);
      } catch (error) {
        console.error("Error fetching assessment: ", error);
      }
    };

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

    fetchAssessment();
    fetchBatchNames();
  }, [id]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const baseUrl = import.meta.env.VITE_SCHEDULE_ASS_URL;
      await axios.put(`${baseUrl}/assessments/${id}`, assessment);
      window.alert("Assessment updated successfully!");
      navigate("/assessment");
    } catch (error) {
      console.error("Error updating assessment: ", error);
    }
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <div className="p-4 text-black">
      <div className="">
        
        <h1 className="text-blue-900 text-4xl font-bold mb-4">UPDATE ASSESSMENT</h1>
        <form onSubmit={handleSubmit} className="text-left ml-60 mt-12 space-y-4">
          <div className="flex items-center">
            <label htmlFor="assessmentName" className="mr-2">Assessment Name:</label>
            <input
              type="text"
              id="assessmentName"
              value={assessment.assessmentName}
              onChange={(e) =>
                setAssessment({ ...assessment, assessmentName: e.target.value })
              }
              className="text-center bg-gray-100 px-2 py-2 ml-12 shadow-lg rounded-full text-black w-1/2 focus:border-transparent focus:outline-none"
              required
            />
          </div>
          <div className="flex items-center">
            <label htmlFor="courseName" className="mr-2">Subject Name:</label>
            <input
              type="text"
              id="courseName"
              value={assessment.courseName}
              onChange={(e) =>
                setAssessment({
                  ...assessment,
                  courseName: e.target.value,
                })
              }
              className="text-center bg-gray-100 px-2 py-2 ml-20 shadow-lg rounded-full text-black w-1/2 focus:border-transparent focus:outline-none"
              required
            />
          </div>
          <div className="flex items-center">
            <label htmlFor="targetBatch" className="mr-2">Target Batch:</label>
            <select
              id="targetBatch"
              value={assessment.targetBatch}
              onChange={(e) =>
                setAssessment({ ...assessment, targetBatch: e.target.value })
              }
              className="text-center bg-gray-100 ml-24 px-2 py-2 shadow-lg rounded-full text-black w-1/2 focus:border-transparent focus:outline-none"
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
          <div className="flex items-center">
            <label htmlFor="startTime" className="mr-2">Start Date and Time:</label>
            <input
              type="datetime-local"
              id="startTime"
              value={assessment.startTime}
              min={new Date().toISOString().slice(0, 16)}
              onChange={(e) =>
                setAssessment({ ...assessment, startTime: e.target.value })
              }
              className="text-center bg-gray-100 px-2 py-2 ml-10 shadow-lg rounded-full text-black w-1/2 focus:border-transparent focus:outline-none"
              required
            />
          </div>
          <div className="flex items-center">
            <label htmlFor="endTime" className="mr-2">End Date and Time:</label>
            <input
              type="datetime-local"
              id="endTime"
              value={assessment.endTime}
              min={assessment.startTime}
              onChange={(e) =>
                setAssessment({ ...assessment, endTime: e.target.value })
              }
              className="text-center bg-gray-100 px-2 py-2 ml-12 shadow-lg rounded-full text-black w-1/2 focus:border-transparent focus:outline-none"
              required
            />
          </div>
          <div className="flex items-center">
            <label htmlFor="durationMinutes" className="mr-2">Duration (in minutes):</label>
            <input
              type="number"
              id="durationMinutes"
              value={assessment.durationMinutes}
              onChange={(e) =>
                setAssessment({ ...assessment, durationMinutes: e.target.value })
              }
              className="text-center bg-gray-100 px-2 py-2 ml-8 shadow-lg rounded-full text-black w-1/2 focus:border-transparent focus:outline-none"
              required
            />
          </div>
          <div className="flex items-center">
            <label htmlFor="numberOfQuestions" className="mr-2">Number of Questions:</label>
            <input
              type="number"
              id="numberOfQuestions"
              value={assessment.numberOfQuestions}
              onChange={(e) =>
                setAssessment({
                  ...assessment,
                  numberOfQuestions: e.target.value,
                })
              }
              className="text-center bg-gray-100 px-2 py-2 ml-8 shadow-lg rounded-full text-black w-1/2 focus:border-transparent focus:outline-none"
              required
            />
          </div>
          <div className="flex items-center">
            <label htmlFor="numberOfViolations" className="mr-2">Number of Violations:</label>
            <input
              type="number"
              id="numberOfViolations"
              value={assessment.numberOfViolations}
              onChange={(e) =>
                setAssessment({ ...assessment, numberOfViolations: e.target.value })
              }
              className="text-center bg-gray-100 px-2 ml-8 py-2 shadow-lg rounded-full text-black w-1/2 focus:border-transparent focus:outline-none"
              required
            />
          </div>
          <div className="flex items-center">
            <label htmlFor="passingPercentage" className="mr-2">Passing Percentage:</label>
            <input
              type="number"
              id="passingPercentage"
              value={assessment.passingPercentage}
              onChange={(e) =>
                setAssessment({
                  ...assessment,
                  passingPercentage: e.target.value,
                })
              }
              className="text-center bg-gray-100 px-2 py-2 ml-12 mb-8 shadow-lg rounded-full text-black w-1/2 focus:border-transparent focus:outline-none"
              required
            />
          </div>
          <button
            type="submit"
            className="bg-blue-900 text-white px-4 py-2 ml-72 rounded-full transition-colors"
          >
            Update Assessment
          </button>
        </form>
      </div>
    </div>
  );
};

export default UpdateAssessment;
