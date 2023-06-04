import React, { useEffect, useState, useRef } from "react";
import "./AllCourses.css";
import { AiFillSignal } from "react-icons/ai";
import { useNavigate } from "react-router-dom";
import getAllCourses from "services/courseService/getAllCourses";
import getDifficulties from "services/difficulty/getDifficulties";
import { AiTwotoneFilter } from "react-icons/ai";
import getAllCoursesByDifficulties from "services/courseService/getAllCoursesByDifficulties";
import getAllLessonsOfACourse from "services/courseService/getAllLessonsOfACourse";
import checkIfCourseIsCompleted from "services/userService/user-course/checkIfCourseIsCompleted";
import isUserEnrolledToCourse from "services/userService/user-course/isUserEnrolledToCourse";

function AllCourses() {
  const [courses, setCourses] = useState([]);
  const [difficulties, setDifficulties] = useState([]);
  const [checkedDifficulties, setCheckedDifficulties] = useState([]);
  const [isCompletionFetched, setIsCompletionFetched] = useState(false);

  const userId = parseInt(localStorage.getItem("userId"));

  const navigate = useNavigate();
  const filterRef = useRef(null);

  const getCourses = async () => {
    const result = await getAllCourses();
    setCourses(result);
  };

  const getAllDifficulties = async () => {
    const result = await getDifficulties();
    setDifficulties(result);
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

  const getCoursesByDifficulty = async (checkedDifficulties) => {
    if (checkedDifficulties.length === 0) {
      getCourses();
      setIsCompletionFetched(false)
      
    } else {
      try {
        const result = await getAllCoursesByDifficulties(checkedDifficulties);
        setCourses(result);
        setIsCompletionFetched(false)
      } catch (error) {
        console.error(error);
      }
    }
  };

  const fetchCompletionStatus = async () => {
    console.log("in fetch")
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
    getCourses();
    getAllDifficulties();
  }, []);

  useEffect(() => {
    getCoursesByDifficulty(checkedDifficulties);
  }, [checkedDifficulties]);

  useEffect(() => {
    window.addEventListener("scroll", handleScroll);
    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, []);

  const handleScroll = () => {
    const filterSection = filterRef.current;
    const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
    const isScrollingDown = scrollTop > 0;

    if (isScrollingDown) {
      filterSection.classList.add("fixed-filter");
    } else {
      filterSection.classList.remove("fixed-filter");
    }
  };

  const handleItemClick = (itemId) => {
    navigate(`/auth/courses/${itemId}`);
  };

  return (
    <div className="list-container-courses">
      <div className="filter-section" ref={filterRef}>
        <p className="filter-heading">
          <AiTwotoneFilter /> Filter
        </p>
        {difficulties.map((difficulty, index) => (
          <label key={index}>
            <input
              type="checkbox"
              id={index.toString()}
              onChange={(e) => {
                const checked = e.target.checked;
                const difficulty = difficulties[index];
                setCheckedDifficulties((prevCheckedDifficulties) => {
                  if (checked) {
                    return [...prevCheckedDifficulties, difficulty];
                  } else {
                    return prevCheckedDifficulties.filter(
                      (checkedDifficulty) => checkedDifficulty !== difficulty
                    );
                  }
                });
              }}
              checked={checkedDifficulties.includes(difficulty)}
            />
            <span>{difficulty}</span>
          </label>
        ))}
      </div>
      <div className="all-courses-section">
        {courses.map((item) => {
          const isEnrolled = item.isEnrolled || false;
          const isCompleted = item.isCompleted || false;
          const isFiltered = checkedDifficulties.length > 0 && !checkedDifficulties.includes(item.difficulty);
          console.log("course name " + item.name + " comp " + item.isCompleted)
          return (
            <div
              key={item.id}
              className={`list-item ${isFiltered ? "" : (isCompleted && isEnrolled ? "completed" : "")}`}
              onClick={() => handleItemClick(item.id)}
            >
              <div className="list-item-header">Course</div>
              <h3 className="list-item-heading">{item.name}</h3>
              <p className="list-item-description">{item.description}</p>
              <div className="list-item-footer">
                <AiFillSignal />
                {item.difficulty}
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}

export default AllCourses;
