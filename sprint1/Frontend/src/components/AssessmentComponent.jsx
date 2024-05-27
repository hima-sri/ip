
import React, { useState, useEffect } from "react";
import axios from "axios";
import { Link } from "react-router-dom";

const AssessmentComponent = () => {
  const [assessments, setAssessments] = useState([]);
  const [selectedOption, setSelectedOption] = useState("All");
  const [searchText, setSearchText] = useState("");
  const [searchPerformed, setSearchPerformed] = useState(false);
  const [matchesFound, setMatchesFound] = useState(true);

  useEffect(() => {
    fetchAssessments(selectedOption);
  }, [selectedOption]);

  const fetchAssessments = async (option) => {
    try {
      const baseUrl = import.meta.env.VITE_SCHEDULE_ASS_URL;
      let url = "";
      if (option === "All") {
        url = `${baseUrl}/assessments`;
      } else if (option === "Active") {
        url = `${baseUrl}/assessments/active`;
      } else if (option === "Completed") {
        url = `${baseUrl}/assessments/completed`;
      }

      const response = await axios.get(url);
      setAssessments(response.data);
    } catch (error) {
      console.error("Error fetching assessments: ", error);
    }
  };

  const handleOptionChange = (e) => {
    setSelectedOption(e.target.value);
  };

  const handleDelete = async (id) => {
    const isConfirmed = window.confirm(
      "Are you sure you want to delete this assessment?"
    );
    if (!isConfirmed) {
      return;
    }

    try {
      const baseUrl = import.meta.env.VITE_SCHEDULE_ASS_URL;
      await axios.delete(`${baseUrl}/assessments/delete/${id}`);
      fetchAssessments(selectedOption);
      window.alert("Assessment deleted successfully!");
    } catch (error) {
      console.error("Error deleting assessment: ", error);
    }
  };

  const handleSearchChange = async (e) => {
    const searchText = e.target.value;
    setSearchText(searchText);

    try {
      const baseUrl = import.meta.env.VITE_SCHEDULE_ASS_URL;
      const response = await axios.get(`${baseUrl}/your-search-endpoint`, {
        params: {
          searchText: searchText
        }
      });

      const data = response.data;
      setAssessments(data);
      setMatchesFound(data.length > 0);
    } catch (error) {
      console.error("Error fetching filtered assessments: ", error);
    }
  };

  const filteredAssessments = Array.isArray(assessments)
    ? assessments.filter(
        (assessment) =>
          assessment.assessmentName &&
          assessment.assessmentName
            .toLowerCase()
            .includes(searchText.toLowerCase())
      )
    : [];

  return (
    <div className="p-1 text-black bg-white">
      <div className="p-1">
        <div className="mb-4">
          <h1 className="text-blue-900 text-4xl mt-2 font-bold mb-4 ">
            SCHEDULED ASSESSMENTS
          </h1>
          {/* Navbar */}
          <div className="flex items-center mt-12 md-3 rounded-full shadow-lg shadow-xl hover:shadow-2xl justify-end bg-gray-200 px-4 py-4">
            <div className="mr-auto">
              <select
                className="appearance-none text-white bg-gray-600 text-center px-4 py-2 rounded-full shadow-lg shadow-xl hover:shadow-2xl ml-2 focus:outline-none focus:border-blue-500"
                value={selectedOption}
                onChange={handleOptionChange}
              >
                <option value="All">All Assessments</option>
                <option value="Active">Active Assessments</option>
                <option value="Completed">Completed Assessments</option>
              </select>
            </div>

            <input
              type="text"
              placeholder="Search by Name..."
              className="px-2 py-2 text-center ml-2 mr-4 rounded-full focus:outline-none focus:border-blue-500"
              value={searchText}
              onChange={handleSearchChange}
              style={{ backgroundColor: "white", color: "#365486" }}
            />
            <Link to="/ScheduledAssessments">
              <button className="bg-blue-900 mr-2 rounded-full text-white shadow-lg shadow-xl hover:shadow-2xl hover:bg-blue-900 px-4 py-2 ml-2">
                + Schedule New
              </button>
            </Link>
          </div>
        </div>
        <div className="flex flex-wrap -mx-2">
          {searchPerformed && !matchesFound && <div>No match found.</div>}
          {filteredAssessments.map((assessment) => (
            <div
              key={assessment.id}
              className="w-1/2 px-2 mb-4 mt-8 "
            >
              <div
                className="p-2 shadow-lg shadow-xl hover:shadow-2xl rounded-lg bg-gray-200"
              >
                <div className="flex justify-between mt-2 items-center mb-2">
                  <div className="assessment text-blue-900 font-bold text-lg">{assessment.assessmentName}</div>
                  <div className="flex items-center">
                    <span>Enable:</span>
                    <label className="toggle-switch relative">
                      <input
                        type="checkbox"
                        className="sr-only"
                        checked={assessment.status === "Active"}
                        readOnly
                      />
                      <div>
                        <input
                          className="mr-2 mt-[0.3rem] h-3.5 w-8 appearance-none rounded-[0.4375rem] bg-neutral-300 before:pointer-events-none before:absolute before:h-3.5 before:w-3.5 before:rounded-full before:bg-transparent before:content-[''] after:absolute after:z-[2] after:-mt-[0.1875rem] after:h-5 after:w-5 after:rounded-full after:border-none after:bg-neutral-100 after:shadow-[0_0px_3px_0_rgb(0_0_0_/_7%),_0_2px_2px_0_rgb(0_0_0_/_4%)] after:transition-[background-color_0.2s,transform_0.2s] after:content-[''] checked:bg-green-500 checked:after:absolute checked:after:z-[2] checked:after:-mt-[3px] checked:after:ml-[1.0625rem] checked:after:h-5 checked:after:w-5 checked:after:rounded-full checked:after:border-none checked:after:bg-primary checked:after:shadow-[0_3px_1px_-2px_rgba(0,0,0,0.2),_0_2px_2px_0_rgba(0,0,0,0.14),_0_1px_5px_0_rgba(0,0,0,0.12)] checked:after:transition-[background-color_0.2s,transform_0.2s] checked:after:content-[''] hover:cursor-pointer focus:outline-none focus:ring-0 focus:before:scale-100 focus:before:opacity-[0.12] focus:before:shadow-[3px_-1px_0px_13px_rgba(0,0,0,0.6)] focus:before:transition-[box-shadow_0.2s,transform_0.2s] focus:after:absolute focus:after:z-[1] focus:after:block focus:after:h-5 focus:after:w-5 focus:after:rounded-full focus:after:content-[''] checked:focus:border-primary checked:focus:bg-primary checked:focus:before:ml-[1.0625rem] checked:focus:before:scale-100 checked:focus:before:shadow-[3px_-1px_0px_13px_#3b71ca]"
                          type="checkbox"
                          role="switch"
                          id="flexSwitchCheckDefault"
                        />
                      </div>
                    </label>
                  </div>
                </div>
                <div className="text-sm text-gray-800 ">
                  <div>Subject Name: {assessment.courseName}</div>
                  <div>Target Batch: {assessment.targetBatch}</div>
                  <div>Start Date & Time: {new Date(assessment.startTime).toLocaleString()}</div>
                  <div>End Date & Time: {new Date(assessment.endTime).toLocaleString()}</div>
                  <div>Duration: {assessment.durationMinutes} minutes</div>
                  <div>Number of Questions: {assessment.numberOfQuestions}</div>
                  <div>Passing Percentage: {assessment.passingPercentage}%</div>
                </div>
                <div className="flex justify-end mt-2">
                  <button
                    className="bg-blue-900 w-40 h-10 mr-80 text-white px-2 py-1 rounded-full"
                  >
                    Generate QP
                  </button>
                  <Link to={`/UpdateAssessment/${assessment.id}`}>
                    <button className="bg-blue-900 w-24 h-10 text-white hover:bg-blue-900 px-2 py-1 rounded-full mr-2">
                      Update
                    </button>
                  </Link>
                  <button
                    className="bg-red-800 w-24 h-10 text-white px-2 py-1 rounded-full"
                    onClick={() => handleDelete(assessment.id)}
                  >
                    Delete
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default AssessmentComponent;
