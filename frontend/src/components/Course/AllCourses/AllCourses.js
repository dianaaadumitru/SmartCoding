import React, { useEffect, useState, useRef } from "react";
import "./AllCourses.css";
import { AiFillSignal } from "react-icons/ai";
import { useNavigate } from "react-router-dom";
import getAllCourses from "services/courseService/getAllCourses";
import getDifficulties from "services/difficulty/getDifficulties";
import { AiTwotoneFilter } from "react-icons/ai";
import getAllCoursesByDifficulties from "services/courseService/getAllCoursesByDifficulties";

function AllCourses() {
  const [courses, setCourses] = useState([]);
  const [difficulties, setDifficulties] = useState([]);
  const [checkedDifficulties, setCheckedDifficulties] = useState([]);
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

  const getCoursesByDifficulty = async (checkedDifficulties) => {
    if (checkedDifficulties.length === 0) {
      getCourses();
    } else {
      try {
        const result = await getAllCoursesByDifficulties(checkedDifficulties);
        setCourses(result);
      } catch (error) {
        console.error(error);
      }
    }
  };


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
        <p className="filter-heading"><AiTwotoneFilter /> Filter</p>
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
        {courses.map((item) => (
          <div
            key={item.id}
            className="list-item-courses"
            onClick={() => handleItemClick(item.id)}
          >
            <div className="list-item-header-courses">Course</div>
            <h3 className="list-item-heading-courses">{item.name}</h3>
            <p className="list-item-description-courses">{item.description}</p>
            <div className="list-item-footer-courses">
              <AiFillSignal />
              {item.difficulty}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default AllCourses;
