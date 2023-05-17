import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from 'react-router-dom';
import './CoursePage.css';
import HomePageNavBar from "pages/homePage/homePageNavBar/HomePageNavBar";
import getCourseById from "services/courseService/getCourseById";
import { AiFillSignal } from "react-icons/ai";

function CoursePage() {
  const navigate = useNavigate();
  const { courseId } = useParams();
  const [course, setCourse] = useState({
    id: courseId,
    name: '',
    description: '',
    difficulty: '',
    courseType: ''
  });

  const getCourse = async () => {
    const result = await getCourseById(courseId)
    setCourse(result)
  }

  useEffect(() => {
    getCourse()
  }, [])

  const handleClick = async () => navigate('/signin')

  return (
    <div className="page-section-course">
      <HomePageNavBar />
      <div className="course-container">
        <div className="course-rectangle">
          <div className="rectangle-header">
            <h2 className="header-text">Course</h2>
          </div>
          <h2 className="course-name">{course.name}</h2>
          <p className="course-description">{course.description}</p>
          <button className="rectangle-button start-btn" onClick={() => handleClick()}>
            Button
          </button>
        </div>
        <div className="course-rectangle-inline">
          <p className="course-difficulty-text"><AiFillSignal />{course.difficulty}</p>
        </div>
        <div className="list-rectangle">
          <h2 className="list-header">List Section</h2>
          <ul className="list-items">
            <li>Item 1</li>
            <li>Item 2</li>
            <li>Item 3</li>
          </ul>
        </div>
      </div>
    </div>
  );
}

export default CoursePage;
