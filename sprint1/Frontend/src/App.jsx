import "./App.css";
// import "./components/styles.css";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import HomePage from "./components/HomePage";

import AssessmentComponent from "./components/AssessmentComponent";
import ScheduleAssessmentComponent from "./components/ScheduleAssessmentComponent";
import UpdateAssessment from "./components/UpdateAssessment";

import QuestionsPage from "./components/managequestions/QuestionsPage";
import EditQuestionComponent from "./components/managequestions/EditQuestionComponent";
import ManualUpload from "./components/manualupload/ManualUpload";
import { Outlet } from "react-router-dom"

import Converter from './components/excelUpload/Converter';
import QuestionLayout from './components/excelUpload/QuestionLayout';

function App() {

  return (
    <Router>
      <Routes>


        <Route path="/" element={<HomePage />}>
          <Route path="assessment" element={<AssessmentComponent />} />

          <Route path="Questions" element={<QuestionsPage />} />
          <Route path="/ManualUpload" element={<ManualUpload />} />
          <Route path='upload' exact element={<Converter />} />
          <Route path="/ScheduledAssessments" element={<ScheduleAssessmentComponent />} />
          <Route path="/UpdateAssessment/:id" element={<UpdateAssessment />} />
          <Route path="/EditQuestion" element={<EditQuestionComponent />} />

          <Route path='/upload' exact element={<Converter />} />
          <Route path='/qlayout' exact element={<QuestionLayout />} />


        </Route>
        {/* <Route path="/" element={<HomePage />} />
        <Route path="/assessment" element={<AssessmentComponent />} /> */}
        {/* <Route path="/ScheduledAssessments" element={<ScheduleAssessmentComponent />} />
        <Route path="/UpdateAssessment/:id" element={<UpdateAssessment />} /> */}

        {/* <Route path="/Questions" element={<QuestionsPage />} /> */}
        {/* <Route path="/EditQuestion" element={<EditQuestionComponent />} /> */}
        {/* <Route path="/ManualUpload" element={<ManualUpload />} /> */}
        {/* 
        <Route path='/upload' exact element={<Converter />} />
        <Route path='/qlayout' exact element={<QuestionLayout />} /> */}

      </Routes>
    </Router>
  );
}

export default App;