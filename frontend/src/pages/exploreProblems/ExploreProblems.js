import React from "react";
import './ExploreProblems.css';
import AllProblems from "components/Problem/AllProblems/AllProblems";
import NavBarRest from "pages/navBar-restOfApplication/NavBarRest";

function ExploreProblems() {
    return (
        <div className="course-explore-container">
            <NavBarRest />
            <h1 className="page-heading-courses">Explore Problems</h1>
            <AllProblems />
        </div>
    );
}

export default ExploreProblems;
