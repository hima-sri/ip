import { MoreVertical, ChevronLast, ChevronFirst } from "lucide-react"
import { useContext, createContext, useState } from "react"
import thisImage from "./this.jpg";
import { Link, Outlet } from "react-router-dom";
import {React } from 'react';

export default function HomePage({ }) {
  const [showLinks, setShowLinks] = useState(false);
  const [selectedLink, setSelectedLink] = useState(null);

  const handleUploadClick = () => {
    setShowLinks(!showLinks); // Toggle the visibility of additional links
  };

  const handleLinkClick = (link) => {
    setSelectedLink(link);
  };

  return (
    <>
      <div className="flex flex-col h-screen font-sans ">
        {/* <!-- Top Horizontal Component --> */}
        <div className="bg-white h-24 mt-2 flex items-center justify-start fixed top-0  left-0 w-full z-10">
          <img src={thisImage}  className="w-49 h-20 " />
         
        </div>

        {/* <!-- Left and Right Vertical Components in a Row --> */}
        {/* flex items-center justify-center */}
        <div className="flex flex-1 fixed top-28 left-0 right-0 bottom-0 ">
          <div className="bg-white w-1/6 w-1/6 sticky top-20 left-0 h-screen overflow-y-auto shadow-xl">

            <ul className="space-y-2.5 font-semibold sticky text-lg w-full box-border">
              <li>
                <Link 
                  className={`text-black p-2 pl-4 ml-0.5 box-border hover:scale-105 active:bg-blue-900 active:rounded-l-lg rounded-l-lg block w-full active:text-white ${selectedLink === '/assessment' ? 'bg-blue-900 text-white' : ''}`}
                  to="/assessment"
                  onClick={() => handleLinkClick('/assessment')}
                >
                  Curate Assessments
                </Link>
              </li>
              <li>
                <Link 
                  className={`text-black p-2 pl-4 ml-0.5 box-border hover:scale-105 active:bg-blue-900 active:rounded-l-lg rounded-l-lg block w-full active:text-white ${selectedLink === '/Questions' ? 'bg-blue-900 text-white' : ''}`}
                  to="/Questions"
                  onClick={() => handleLinkClick('/Questions')}
                >
                  Manage Questions
                </Link>
              </li>
  
              <Link 
                className={`text-black p-2 pl-4 ml-0.5  box-border hover:scale-105 active:bg-blue-900 active:rounded-l-lg rounded-l-lg block w-full active:text-white active:pl-2 ${selectedLink === '/upload'  || selectedLink === '/ManualUpload'  ? 'bg-blue-900 text-white' : ''}`}
                onClick={handleUploadClick}
              >
                Upload Questions
              </Link>
              {showLinks && (
                <ul className="space-y-2 font-semibold">
                  <li>
                    <Link 
                      className={`text-black box-border p-2 pl-4 ml-6  hover:scale-105 active:bg-blue-900 active:rounded-l-lg rounded-l-lg block w-full active:text-white active:pl-2 ${selectedLink === '/ManualUpload' ? 'bg-blue-900 text-white' : ''}`}
                      to="/ManualUpload"
                      onClick={() => handleLinkClick('/ManualUpload')}
                    >
                      Manual Upload
                    </Link>
                  </li>
                  <li>
                    <Link 
                      className={`text-black p-2 pl-4 ml-6 box-border hover:scale-105 active:bg-blue-900 active:rounded-l-lg rounded-l-lg block w-full active:text-white active:pl-2 ${selectedLink === '/upload' ? 'bg-blue-900 text-white' : ''}`}
                      to="/upload"
                      onClick={() => handleLinkClick('/upload')}
                    >
                      Excel Upload
                    </Link>
                  </li>
                </ul>
              )}
            </ul>
          </div>
          {/* <!-- Right Vertical Component --> */}
          <div className="bg-blue-50 p-4 w-full h-full overflow-y-scroll">
            <Outlet  />
          </div>
        </div>
      </div>
    </>
  )
}
