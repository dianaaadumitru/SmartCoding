import React from "react";
import './ExploreProblems.css';
import NavBar from "pages/navBar/NavBar";
import AllProblems from "components/Problem/AllProblems/AllProblems";

function ExploreProblems() {
    return (
        <div className="course-explore-container">
            <NavBar />
            <h1 className="page-heading-courses">Explore Problems</h1>
            <AllProblems />
        </div>
    );
}

export default ExploreProblems;
