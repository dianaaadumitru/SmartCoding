import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from 'react-router-dom';
import './CoursePage.css';
import HomePageNavBar from "pages/homePage/homePageNavBar/HomePageNavBar";
import getCourseById from "services/courseService/getCourseById";
import { AiFillSignal } from "react-icons/ai";
import Lesson from "components/Lesson/Lesson";
import TopCourses from "components/Course/TopCourses";
import getAllLessonsOfACourse from "services/courseService/getAllLessonsOfACourse";

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
    const [lessons, setLessons] = useState([]);

    const getLessons = async () => {
        const result = await getAllLessonsOfACourse(courseId);
        setLessons(result)
    }

    const getCourse = async () => {
        const result = await getCourseById(courseId)
        setCourse(result)
    }

    useEffect(() => {
        getCourse()
        getLessons()
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
                        Start
                    </button>
                </div>
                <div className="course-rectangle-inline">
                    <p className="course-difficulty-text"><AiFillSignal />{course.difficulty}</p>
                </div>

                <div className="list-rectangle">
                    <h2 className="list-header">Lessons</h2>
                    <ul className="list-items">
                        {lessons.map((item) => (
                            <li key={item.id} className="list-item-lesson">
                                <div className="list-item-header-lesson">{item.name}</div>
                                <div className="list-item-description-lesson">{item.description}</div>
                            </li>
                        ))}
                    </ul>
                    <button className=" list-button">Start</button>
                </div>
            </div>
        </div>
    );
}

export default CoursePage;
