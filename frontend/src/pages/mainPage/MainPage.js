import React, { useEffect, useRef } from "react";
import './MainPage.css'
import NavBar from "pages/navBar/NavBar";
import TopCourses from 'components/Course/TopCourses/TopCourses';
import TopProblems from "components/Problem/TopProblems";
import { useNavigate } from "react-router-dom";

function MainPage() {
    const navigate = useNavigate()

    const targetRef = useRef(null);

    useEffect(() => {
        if (window.location.hash === '#target') {
          targetRef.current.scrollIntoView({ behavior: 'smooth' });
        }
      }, []);

      const handleExploreCoursesOnClick = () => navigate('/courses')


    return (
        <div className="page-section-main">
            <NavBar targetRef={targetRef}/>
            <section className='explore-section'>
                <h2 ref={targetRef} className='start-learning'>Start learning</h2>
                <p className='top-courses'>Top courses: </p>
                <TopCourses />
                <button className="create-account-btn" onClick={handleExploreCoursesOnClick}>Explore full catalog</button>
                <p className='top-courses'>Top problems: </p>
                <TopProblems />
                <button className="create-account-btn" >Explore full catalog</button>
            </section>
        </div>
    )
}

export default MainPage;