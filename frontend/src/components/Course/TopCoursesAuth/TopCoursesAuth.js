import React, { useEffect, useState } from "react";
import './TopCoursesAuth.css'
import { AiFillSignal } from "react-icons/ai";
import { useNavigate } from "react-router-dom";
import getTopCourses from "services/courseService/getTopCourses";
import getAllLessonsOfACourse from "services/courseService/getAllLessonsOfACourse";
import checkIfCourseIsCompleted from "services/userService/user-course/checkIfCourseIsCompleted";
import isUserEnrolledToCourse from "services/userService/user-course/isUserEnrolledToCourse";

function TopCoursesAuth() {
    const [courses, setCourses] = useState([]);
    const [isCompletionFetched, setIsCompletionFetched] = useState(false);
    const [isEnrolled, setIsEnrolled] = useState(false);

    const userId = parseInt(localStorage.getItem("userId"));

    const navigate = useNavigate();

    const itemsPerPage = 4;
    const [currentPage, setCurrentPage] = useState(1);

    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const paginatedItems = courses.slice(startIndex, endIndex);

    const totalPages = Math.ceil(courses.length / itemsPerPage);

    const goToPage = (page) => {
        setCurrentPage(page);
    };

    const getCourses = async () => {
        const result = await getTopCourses();
        setCourses(result)
    }

    const isUserEnrolled = async (courseId) => {
        const result = await isUserEnrolledToCourse(userId, courseId);
        return result;
    };

    const checkCourseCompletionStatus = async (courseId) => {
        const lessons = await getAllLessonsOfACourse(courseId);
        const isCompleted = await checkIfCourseIsCompleted(userId, courseId, lessons.length);
        return isCompleted;
    };

    const fetchCompletionStatus = async () => {
        const promises = courses.map((item) => checkCourseCompletionStatus(item.id));
        const completionStatuses = await Promise.all(promises);

        const promises2 = courses.map((item) => isUserEnrolled(item.id))
        const completionStatuses2 = await Promise.all(promises2);

        setCourses((prevCourses) => {
            const updatedCourses = prevCourses.map((course, index) => ({
                ...course,
                isCompleted: completionStatuses[index],
                isEnrolled: completionStatuses2[index],
            }));
            return updatedCourses;
        });
        setIsCompletionFetched(true);
    };

    useEffect(() => {
        if (courses.length > 0 && !isCompletionFetched) {
            fetchCompletionStatus();
        }
    }, [courses, isCompletionFetched]);

    useEffect(() => {
        getCourses()
    }, [])

    const handleItemClick = (itemId) => {
        navigate(`/auth/courses/${itemId}`);
    };

    return (
        <div className="list-container">
            <div className="pagination">
                {currentPage > 1 && (
                    <button className="pagination-button" onClick={() => goToPage(currentPage - 1)}>
                        {'<'}
                    </button>
                )}
                {paginatedItems.map((item) => {
                    const course = courses.find((c) => c.id === item.id);
                    const isCompleted = course ? course.isCompleted || false : false;
                    const isEnrolled = course ? course.isEnrolled || false : false;
                    return (
                        <div
                            key={item.id}
                            className={`list-item ${isCompleted && isEnrolled ? "completed" : ""}`}
                            onClick={() => handleItemClick(item.id)}
                        >
                            <div className="list-item-header">Course</div>
                            <h3 className="list-item-heading">{item.name}</h3>
                            <p className="list-item-description">{item.courseType}</p>
                            <div className="list-item-footer">
                                <AiFillSignal />
                                {item.difficulty}
                            </div>
                        </div>
                    );
                })}
                {currentPage < totalPages && (
                    <button className="pagination-button" onClick={() => goToPage(currentPage + 1)}>
                        {'>'}
                    </button>
                )}
            </div>
        </div>
    );
}

export default TopCoursesAuth;