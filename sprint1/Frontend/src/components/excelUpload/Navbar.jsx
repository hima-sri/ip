
import React from "react";


const Navbar = ({ showBackButton }) => {
  return (
    <nav className="bg-white-600 text-white flex justify-between items-center p-2 w-full">
      <div className="flex items-center w-full">
        {/* GIF on the left side */}
        {/* <img src={profileGif} alt="Profile GIF" className="w-32 h-8" /> */}
      </div>
      {showBackButton && (
        <div className="back-button">
          { /*button for a cause*/}
          <button className="px-4 py-2 bg-blue-600 text-white rounded cursor-pointer hover:bg-blue-800">Back</button>
        </div>
      )}
    </nav>
  );
};

export default Navbar;

