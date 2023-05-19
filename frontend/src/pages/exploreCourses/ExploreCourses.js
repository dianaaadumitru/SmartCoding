import React from "react";
import './ExploreCourses.css';
import NavBar from "pages/navBar/NavBar";
import AllCourses from "components/Course/AllCourses/AllCourses";

function ExploreCourses() {
    return (
        <div className="course-explore-container">
            <NavBar />
            <h1 className="page-heading">Explore Courses</h1>
            <div className="course-explore-content">
                <div className="filter-section">
                    <p>Filters</p>
                    <label>
                        <input type="checkbox" id="checkbox1" />
                        Checkbox 1
                    </label>
                    <label>
                        <input type="checkbox" id="checkbox2" />
                        Checkbox 2
                    </label>
                    <label>
                        <input type="checkbox" id="checkbox3" />
                        Checkbox 3
                    </label>
                </div>
                <div className="all-courses-section">
                    <AllCourses />
                </div>
            </div>
        </div>
    );
}

export default ExploreCourses;
