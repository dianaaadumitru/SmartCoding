import React, { useEffect, useState } from "react";
import { AiFillSignal } from "react-icons/ai";
import { useNavigate } from "react-router-dom";
import getAllLessonsOfACourse from "services/courseService/getAllLessonsOfACourse";
import checkIfCourseIsCompleted from "services/userService/user-course/checkIfCourseIsCompleted";
import getAllEnrolledCourses from "services/userService/user-course/getAllEnrolledCourses";
import "./EnrolledCourses.css";

function EnrolledCourses() {
  const [courses, setCourses] = useState([]);
  const [isCompletionFetched, setIsCompletionFetched] = useState(false);
  const navigate = useNavigate();
  const userId = parseInt(localStorage.getItem("userId"));

  const itemsPerPage = 3;
  const [currentPage, setCurrentPage] = useState(1);

  const startIndex = (currentPage - 1) * itemsPerPage;
  const endIndex = startIndex + itemsPerPage;
  const paginatedItems = courses.slice(startIndex, endIndex);

  const totalPages = Math.ceil(courses.length / itemsPerPage);

  const getCourses = async () => {
    const result = await getAllEnrolledCourses(userId);
    setCourses(result);
  };

  const checkCourseCompletionStatus = async (courseId) => {
    const lessons = await getAllLessonsOfACourse(courseId);
    const isCompleted = await checkIfCourseIsCompleted(userId, courseId, lessons.length);
    return isCompleted;
  };

  const fetchCompletionStatus = async () => {
    const promises = courses.map((item) => checkCourseCompletionStatus(item.id));
    const completionStatuses = await Promise.all(promises);

    setCourses((prevCourses) => {
      const updatedCourses = prevCourses.map((course, index) => ({
        ...course,
        isCompleted: completionStatuses[index],
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
    getCourses();
  }, []);

  const handleItemClick = (itemId) => {
    navigate(`/auth/courses/${itemId}`);
  };

  return (
    <div className="list-container">
      <div className="pagination">
        {currentPage > 1 && (
          <button className="pagination-button" onClick={() => setCurrentPage(currentPage - 1)}>
            {"<"}
          </button>
        )}
        {paginatedItems.map((item) => {
          const course = courses.find((c) => c.id === item.id);
          const isCompleted = course ? course.isCompleted || false : false;
          return (
            <div
              key={item.id}
              className={`list-item ${isCompleted ? "completed" : ""}`}
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
          <button className="pagination-button" onClick={() => setCurrentPage(currentPage + 1)}>
            {">"}
          </button>
        )}
      </div>
    </div>
  );
}

export default EnrolledCourses;
