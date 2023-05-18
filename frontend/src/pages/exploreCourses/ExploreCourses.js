import React from "react";
import './ExploreCourses.css';
import NavBar from "pages/navBar/NavBar";
import AllCourses from "components/Course/AllCourses/AllCourses";

function ExploreCourses() {
    return (
        <div className="page-section-courses">
            <NavBar />
            <div className="courses-container">
                <AllCourses />
            </div>
        </div>
    )
}

export default ExploreCourses;