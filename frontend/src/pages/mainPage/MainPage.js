import React, { useEffect, useRef } from "react";
import "./MainPage.css";
import NavBar from "pages/navBar/NavBar";
import TopCourses from "components/Course/TopCourses/TopCourses";
import TopProblems from "components/Problem/TopProblems/TopProblems";
import { useNavigate } from "react-router-dom";
import TopCoursesAuth from "components/Course/TopCoursesAuth/TopCoursesAuth";
import TopProblemsAuth from "components/Problem/TopProblemsAuth/TopProblemsAuth";
import DeveloperComponent from "components/Developer/DeveloperComponent";

function MainPage() {
  const navigate = useNavigate();

  const exploreRef = useRef(null);
  const developerRef = useRef(null);

  useEffect(() => {
    if (window.location.hash === "#explore") {
      exploreRef.current.scrollIntoView({ behavior: "smooth" });
    }
    if (window.location.hash === "#developer") {
      developerRef.current.scrollIntoView({ behavior: "smooth" });
    }
  }, []);

  const handleExploreCoursesOnClick = () => navigate("/courses");
  const handleExploreProblemsOnClick = () => navigate("/problems");

  return (
    <div className="page-section-main">
      <NavBar exploreRef={exploreRef} developerRef={developerRef} />
      <section className="explore-section">
        <h2 ref={exploreRef} className="start-learning">
          Start learning
        </h2>
        <p className="top-courses">Top courses: </p>
        <TopCoursesAuth />
        <button
          className="create-account-btn"
          onClick={handleExploreCoursesOnClick}
        >
          Explore full catalog
        </button>
        <p className="top-courses">Top problems: </p>
        <TopProblemsAuth />
        <button
          className="create-account-btn"
          onClick={handleExploreProblemsOnClick}
        >
          Explore full catalog
        </button>
      </section>

      <section className="developer-section">
        <h2 ref={developerRef} className="start-learning">
          Developer
        </h2>
        <DeveloperComponent />
      </section>
    </div>
  );
}

export default MainPage;
