import React from "react";
import './ExploreCourses.css';
import AllCourses from "components/Course/AllCourses/AllCourses";
import NavBarRest from "pages/navBar-restOfApplication/NavBarRest";

function ExploreCourses() {
    return (
        <div className="course-explore-container">
            <NavBarRest />
            <h1 className="page-heading-courses">Explore Courses</h1>
                    <AllCourses />
        </div>
    );
}

export default ExploreCourses;
