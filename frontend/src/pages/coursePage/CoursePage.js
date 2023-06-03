import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from 'react-router-dom';
import './CoursePage.css';
import getCourseById from "services/courseService/getCourseById";
import { AiFillSignal, AiOutlineClockCircle } from "react-icons/ai";
import getAllLessonsOfACourse from "services/courseService/getAllLessonsOfACourse";
import HomePageNavBar from "pages/homePage/homePageNavBar/HomePageNavBar";
import computeTimeNeededToFinishCourse from "services/courseService/computeTimeNeededToFinishCourse";

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
    const [timeToFinish, setTimeToFinish] = useState(0);

    const getLessons = async () => {
        const result = await getAllLessonsOfACourse(courseId);
        const sortedLessons = result.sort((a, b) => a.noLesson - b.noLesson);
        setLessons(sortedLessons);
    }

    const getExpectedTimeToFinishCourse = async() => {
        const result = await computeTimeNeededToFinishCourse(courseId);
        setTimeToFinish(result);
    }

    const getCourse = async () => {
        const result = await getCourseById(courseId)
        setCourse(result)
    }

    useEffect(() => {
        getCourse()
        getLessons()
        getExpectedTimeToFinishCourse()
    }, [])

    const handleClick = async () => navigate('/signin')

    return (
        <div className="page-section-course">
            <HomePageNavBar onExploreClick={undefined} onDeveloperClick={undefined} />

            <div className="course-container">
                <div className="course-rectangle">
                    <div className="rectangle-header">
                        <h2 className="header-text">Course</h2>
                        <h2 className="course-name">{course.name}</h2>
                        <p className="course-description">{course.description}</p>
                        <div className="line"></div>

                        <div className="bottom-section">
                            <div className="left-content">
                                <AiFillSignal className="info-icon" />
                                <div>
                                    <p className="bottom-text">Skill level</p>
                                    <p className="bottom-text second-line">{course.difficulty}</p>
                                </div>
                            </div>
                            <button className="bottom-button" onClick={handleClick}>Start</button>
                            <div className="right-content">
                                <AiOutlineClockCircle className="info-icon" />
                                <div>
                                    <p className="bottom-text">Time to complete</p>
                                    <p className="bottom-text second-line">approx. {timeToFinish} hours</p>
                                </div>
                            </div>
                        </div>

                        <div className="additional-rectangle">
                            <div className="additional-header">Syllabus</div>
                            <div className="additional-content">
                                <p className="additional-description">{lessons.length} lessons</p>
                            </div>
                            <div className="additional-line"></div>

                            {lessons.map((lesson, index) => (
                                <React.Fragment key={lesson.id}>
                                    <div className="additional-content" onClick={handleClick}>
                                        <div className="lesson-item">
                                            <div className="lesson-number">{index + 1}</div>
                                            <div className="lesson-info">
                                                <p className="lesson-name">{lesson.name}</p>
                                                <p className={`lesson-description ${index === lessons.length - 1 ? 'last-element' : ''}`}>{lesson.description}</p>
                                            </div>
                                        </div>
                                    </div>
                                    {index !== lessons.length - 1 && <div className="additional-line"></div>}

                                </React.Fragment>
                            ))}


                        </div>

                        <button className="bottom-button last-padding" onClick={handleClick}>Start</button>


                    </div>
                </div>
            </div>
        </div>
    );
}

export default CoursePage;