import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from 'react-router-dom';
import './CoursePageAuth.css';
import getCourseById from "services/courseService/getCourseById";
import { AiFillSignal, AiOutlineClockCircle, AiOutlineLock } from "react-icons/ai";
import getAllLessonsOfACourse from "services/courseService/getAllLessonsOfACourse";
import NavBar from "pages/navBar/NavBar";
import addCourseToUser from "services/userService/addCourseToUser";
import addEnrolledLessonToUser from "services/userService/user-lesson/addEnrolledLessonToUser";
import isUserEnrolledToCourse from "services/userService/user-course/isUserEnrolledToCourse";
import checkIfAUserLessonIsCompleted from "services/userService/user-lesson/checkIfAUserLessonIsComplete";

function CoursePageAuth() {
    const navigate = useNavigate();
    const [userId, setUserId] = useState(0);
    const { courseId } = useParams();
    const [course, setCourse] = useState({
        id: courseId,
        name: '',
        description: '',
        difficulty: '',
        courseType: '',
        noLesson: 0
    });
    const [lessons, setLessons] = useState([]);
    const [isEnrolled, setIsEnrolled] = useState(false);
    const [startButtonVisible, setStartButtonVisible] = useState(true);

    const getUserId = () => {
        setUserId(parseInt(localStorage.getItem('userId')));
        console.log(parseInt(localStorage.getItem('userId')))
    }

    const getLessons = async () => {
        const result = await getAllLessonsOfACourse(courseId);
        const sortedLessons = result.sort((a, b) => a.noLesson - b.noLesson);
        setLessons(sortedLessons);
    }

    const getCourse = async () => {
        const result = await getCourseById(courseId)
        setCourse(result)
    }

    const isUserEnroled = async () => {
        const result = await isUserEnrolledToCourse(userId, courseId);
        setIsEnrolled(result);
    }

    const enrolUserToCourse = async () => {
        const result = await addCourseToUser(userId, courseId);
    }

    const isLessonCompleted = async (lessonId) => {
        const result = await checkIfAUserLessonIsCompleted(userId, lessonId);
    }

    const handleClick = async () => {
        enrolUserToCourse();
        setStartButtonVisible(false);
        // ... other logic ...
    }

    const handleClickLesson = async (itemId) => navigate(`/auth/lessons/${itemId}`);

    useEffect(() => {
        getCourse();
        getLessons();
        getUserId();
    }, []);

    useEffect(() => {
        isUserEnroled();
    }, [userId]);

    return (
        <div className="page-section-course">
            <NavBar />

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

                            {isEnrolled ? (
                                <p className="already-enrolled-text">You are already enrolled</p>
                            ) : (
                                startButtonVisible && (
                                    <button className="bottom-button" onClick={handleClick}>
                                        Start
                                    </button>
                                )
                            )}

                            <div className="right-content">
                                <AiOutlineClockCircle className="info-icon" />
                                <div>
                                    <p className="bottom-text">Time to complete</p>
                                    <p className="bottom-text second-line">approx. 2 hours</p>
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
                                    <div className="additional-content" onClick={() => handleClickLesson(lesson.id)}>
                                        <div className="lesson-item">

                                            <div className="lesson-addons">
                                                <input type="checkbox" readOnly className="lesson-checkbox" />
                                                <AiOutlineLock className="lesson-icon" />
                                            </div>

                                            <div className="lesson-number">{lesson.noLesson}</div>
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

                        {!isEnrolled && startButtonVisible && (
                            <button className="bottom-button last-padding" onClick={handleClick}>
                                Start
                            </button>
                        )}

                    </div>
                </div>
            </div>
        </div>
    );
}

export default CoursePageAuth;
