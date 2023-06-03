import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import './CoursePageAuth.css';
import getCourseById from "services/courseService/getCourseById";
import { AiFillSignal, AiOutlineClockCircle, AiOutlineLock } from "react-icons/ai";
import getAllLessonsOfACourse from "services/courseService/getAllLessonsOfACourse";
import NavBar from "pages/navBar/NavBar";
import addCourseToUser from "services/userService/user-course/addCourseToUser";
import isUserEnrolledToCourse from "services/userService/user-course/isUserEnrolledToCourse";
import checkIfCourseIsCompleted from "services/userService/user-course/checkIfCourseIsCompleted";
import LessonItem from "components/Lesson/LessonItem";
import getCourseLessonByNoLesson from "services/courseService/getCourseLessonByNoLesson";
import addEnrolledLessonToUser from "services/userService/user-lesson/addEnrolledLessonToUser";
import computeTimeNeededToFinishCourse from "services/courseService/computeTimeNeededToFinishCourse";

function CoursePageAuth() {
    const navigate = useNavigate();
    const [userId, setUserId] = useState(0);
    const { courseId } = useParams();
    const [course, setCourse] = useState({
        id: courseId,
        name: "",
        description: "",
        difficulty: "",
        courseType: "",
        noLesson: 0,
    });
    const [lessons, setLessons] = useState([]);
    const [isEnrolled, setIsEnrolled] = useState(false);
    const [startButtonVisible, setStartButtonVisible] = useState(true);
    const [isLoading, setIsLoading] = useState(false);
    const [isCourseCompleted, setIsCourseCompleted] = useState(false);
    const [timeToFinish, setTimeToFinish] = useState(0);
    localStorage.setItem('courseId', courseId);

    const getUserId = () => {
        setUserId(parseInt(localStorage.getItem("userId")));
    };

    const getLessons = async () => {
        const result = await getAllLessonsOfACourse(courseId);
        const sortedLessons = result.sort((a, b) => a.noLesson - b.noLesson);
        localStorage.setItem('lessonsLength', sortedLessons.length);
        setLessons(sortedLessons);
    };

    const getExpectedTimeToFinishCourse = async () => {
        const result = await computeTimeNeededToFinishCourse(courseId);
        setTimeToFinish(result);
    }

    const getCourse = async () => {
        const result = await getCourseById(courseId);
        setCourse(result);
    };

    const isUserEnroled = async () => {
        const result = await isUserEnrolledToCourse(userId, courseId);
        setIsEnrolled(result);
    };

    const enrolUserToCourse = async () => {
        await addCourseToUser(userId, courseId);
    };

    const checkIfCourseCompleted = async () => {
        const result = await checkIfCourseIsCompleted(userId, courseId, lessons.length);
        setIsCourseCompleted(result);
    }

    const handleClick = async () => {
        setIsLoading(true);
        await enrolUserToCourse();
        setIsLoading(false);
        setStartButtonVisible(false);
        if (lessons.length > 0) {
            addLessonsToUser().then(async () => {
                const result = await getCourseLessonByNoLesson(courseId, 1);
                navigate(`/auth/lessons/${result.id}`);
            })
        }
    };

    const addLessonsToUser = async () => {
        lessons.forEach(async lesson => {
            await addEnrolledLessonToUser(userId, lesson.id, courseId)
        });
    }

    const handleClickLesson = async (itemId) => {
        if (lessons.length > 0)
            navigate(`/auth/lessons/${itemId}`);
    }

    useEffect(() => {

        getUserId();
    }, []);

    useEffect(() => {
        if (userId !== 0) {
            (async () => {
                await getCourse();
                await getLessons();
                await checkIfCourseCompleted();
                await getExpectedTimeToFinishCourse();
            })();
        }
    }, [userId]);

    useEffect(() => {
        if (userId !== 0) {
            (async () => {
                await checkIfCourseCompleted();
                await isUserEnroled();
            })();
        }
    }, [userId, lessons]);

    return (
        <div className="page-section-course">
            <NavBar />

            <div className="course-container">
                <div className="course-rectangle">
                    <div className="rectangle-header">
                        <h2 className="header-text">Course</h2>
                        <h2 className="course-name">
                            {course.name}
                        </h2>
                        {isEnrolled && isCourseCompleted && (
                            <span className="completed-text">Completed</span>
                        )}

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
                                <p className="already-enrolled-text">Enrolled</p>
                            ) : isLoading ? (
                                <div className="loading-indicator"></div>
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
                                    <LessonItem lesson={lesson} userId={userId} courseId={courseId} isEnrolled={isEnrolled} index={index} lessons={lessons} handleClickLesson={handleClickLesson} />
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
