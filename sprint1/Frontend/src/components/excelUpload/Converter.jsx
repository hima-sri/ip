import React, { useState } from "react";
import * as XLSX from "xlsx";
import Papa from "papaparse";
import QuestionLayout from "./QuestionLayout";
import Navbar from "./Navbar"
import { useNavigate, useLocation } from "react-router-dom";
import axios from "axios";
import image from './images/image.png';


const ExcelPage = () => {
  const [questionData, setQuestionData] = useState(null);
  const [previewData, setPreviewData] = useState(false);
  const [questions, setQuestions] = useState([]);
  const [options, setOptions] = useState([]);
  const [answers, setAnswers] = useState([]);
  const [selectedFile, setSelectedFile] = useState(null);
  const [isUploadEnabled, setIsUploadEnabled] = useState(false);
  const navigate = useNavigate();
  const [fileFormatError, setFileFormatError] = useState(false);
  const [showImage, setShowImage] = useState(false);

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    setSelectedFile(file);
    setIsUploadEnabled(true);

    // Check file extension
    if (!file.name.endsWith(".csv") && !file.name.endsWith(".xls") && !file.name.endsWith(".xlsx")) {
      alert("Unsupported file \nPlease upload an Excel file only."); // Display error message as alert
      setFileFormatError(true); // Set file format error state
      return;
    } else {
      setFileFormatError(false); // Reset file format error state
    }
  };

                             
  const toggleImageModal = () => {
    // Create a temporary anchor element
    const anchor = document.createElement('a');
    // Set the href attribute to the URL of the Excel file in your S3 bucket
    anchor.href = 'https://assessment-files.s3.eu-central-1.amazonaws.com/sample-format.xlsx';
    // Set the download attribute to specify the filename
    anchor.download = 'sample-format.xlsx';
    
    // Append the anchor to the body
    document.body.appendChild(anchor);
    
    // Trigger a click event on the anchor
    anchor.click();
    
    // Remove the anchor from the body
    document.body.removeChild(anchor);
};

  const handleClose = () => {
    setShowImage(false);
    navigate(0); // Go back to the previous page
  };

  const handlePreview = () => {
    if (!selectedFile) {
      console.error("No file selected");
      return;
    }

    const reader = new FileReader();

    reader.onload = (event) => {
      const binaryString = event.target.result;

      // Check file extension and parse accordingly
      if (selectedFile.name.endsWith(".csv")) {
        Papa.parse(binaryString, {
          complete: (result) => {
            const csvData = result.data;
            const extractedQuestions = csvData.map(row => row.title);
            setQuestionData(csvData);
            setQuestions(extractedQuestions);
            setPreviewData(true);
          },
          header: true, // Treat first row as header
        });
      } else if (selectedFile.name.endsWith(".xls") || selectedFile.name.endsWith(".xlsx")) {
        const workbook = XLSX.read(binaryString, { type: "binary" });
        const sheetName = workbook.SheetNames[0];
        const sheet = workbook.Sheets[sheetName];
        const excelData = XLSX.utils.sheet_to_json(sheet, { header: 0 });
        const extractedQuestions = excelData.map(row => row.title);
        const extractedAnswers = excelData.map(row => row.answers.split(','));
        setQuestionData(excelData);
        console.log(excelData);
        setAnswers(extractedAnswers)
        setQuestions(extractedQuestions);
        setPreviewData(true);
      } else {
        console.error("Unsupported file format");
      }
    };

    reader.readAsBinaryString(selectedFile);
  };
  const handleUpload = async () => {
    console.log("handleUpload called");

    if (!selectedFile) {
      console.error("No data to upload");
      return;
    }

    // Create a FormData object
    const formData = new FormData();

    // Append the file data to the FormData object
    formData.append('file', selectedFile);

    try {
      console.log(`${import.meta.env.endpoint}`)
      const url = import.meta.env.VITE_QUESTION_SERVICE
      console.log(url)
      const response = await axios.post(`${url}/questions/upload`, formData, {
        // Set headers for multipart/form-data
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });

      if (response.status === 200) {
        alert('File uploaded successfully');
        console.log('File uploaded successfully');
        // Navigate to previous page
        navigate(0); // Go back one step in the history
      } else {
        console.error('Failed to upload file');
      }
    } catch (error) {
      console.error('Error occurred while uploading file:', error);
      // Handle error
    }
  };



  const handleCancel = () => {
    const confirmed = window.confirm("Are you sure you want to cancel?");

    if (confirmed) {
      navigate(0); // Redirect to previous page if user cancels
    }
  };



  return (
    <div className="main min-h-full p-5 text-center" style={{ backgroundColor: '#ffffff' }}>
      {/* Include the Navbar component */}
      <Navbar />
      {questions.length === 0 ? (
        <>
          <h1 className="font-bold text-3xl mb-4">Upload Question Bank </h1>

          <div className="mb-4 flex items-center justify-center space-x-2">
            <h2 className="text-lg font-semibold">Click on Sample to view the format of the excel file:</h2>
            <button onClick={toggleImageModal} className="text-blue-500 underline">Sample</button>
          </div>
          {/* {showImage && (
  <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
    <div className="relative max-w-screen-sm mx-auto">
      <button onClick={handleClose} className="absolute top-0 right-0 -mt-12 -mr-12 p-4 text-red text-xl z-10">&times;</button>
      <img
        src={image}
        alt="Excel Required Fields"
        className="mx-auto transition-transform duration-300 transform hover:scale-150"
        style={{ width: '200%', height: 'auto' }}
      />
    </div>
  </div>
)}
  */}


          <input type="file" onChange={handleFileChange} />
          {selectedFile && (
            <div className="flex justify-center">
              <button onClick={handlePreview} className="px-4 py-2 mt-4 bg-blue-900 text-white rounded cursor-pointer hover:bg-blue-800"> Preview </button>
            </div>
          )}

          {/* {selectedFile && <p>Selected file: {selectedFile.name}</p>} */}
        </>
      ) : (
        <>
          <div className="banner flex place-content-between" id="b1" >
            <div className="title ml-2">
              <h1 className="text-3xl text-blue-900 font-bold mb-2">Questions Preview</h1>
              <h2 className=" text-xs mt-2 mb-2">(Correct options are highlighted)</h2>
            </div>
            <div className="buttons">
              <button onClick={handleUpload} className="px-2 mr-2 py-1 mt-10 bg-green-600 text-white rounded cursor-pointer mr-5 hover:bg-blue-800 hover:text-white transition duration-200 text-sm">Upload</button>
              <button onClick={handleCancel} className="px-2 py-1 mr-40 bg-blue-900 text-white rounded cursor-pointer  hover:bg-blue-800 hover:text-white transition duration-200 text-sm">Cancel</button>

            </div>


          </div>
          <div className="questions">
                {questionData.map((data, index) => (
                  <QuestionLayout
                    key={index}
                    question={data.title}
                    option1={data['Option A']}
                    option2={data['Option B']}
                    option3={data['Option C']}
                    option4={data['Option D']}
                    option5={data['Option E']}
                    option6={data['Option F']}
                    option7={data['Option G']}
                    option8={data['Option H']}
                    subTopic={data.subtopic}
                    qtype={data.questiontype}
                    atype={data.answertype}
                    level={data.level}
                    subject={data["subject"]}
                    answer={answers[index]}
                    qnum={index}
                    
                  />
                )
              )}

          <a href=" #b1" className="h4 rounded-md bg-black text-white p-2" >Top</a>

          </div>
        </>

      )}
    </div>
  ); //return block
}; //function 

export default ExcelPage;