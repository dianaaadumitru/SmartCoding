import React from "react";
import './ExploreCourses.css';
import NavBar from "pages/navBar/NavBar";
import AllCourses from "components/Course/AllCourses/AllCourses";

function ExploreCourses() {
    return (
        <div className="course-explore-container">
            <NavBar />
            <h1 className="page-heading-courses">Explore Courses</h1>
                    <AllCourses />
        </div>
    );
}

export default ExploreCourses;
