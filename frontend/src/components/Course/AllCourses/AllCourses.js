import React, { useEffect, useState, useRef } from "react";
import "./AllCourses.css";
import { AiFillSignal } from "react-icons/ai";
import { useNavigate } from "react-router-dom";
import getAllCourses from "services/courseService/getAllCourses";
import getDifficulties from "services/difficulty/getDifficulties";

function AllCourses() {
  const [courses, setCourses] = useState([]);
  const [difficulties, setDifficulties] = useState([]);
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

  useEffect(() => {
    getCourses();
    getAllDifficulties();
  }, []);

  useEffect(() => {
    window.addEventListener("scroll", handleScroll); // Listen for scroll events
    return () => {
      window.removeEventListener("scroll", handleScroll); // Remove the event listener when the component unmounts
    };
  }, []);

  const handleScroll = () => {
    const filterSection = filterRef.current;
    const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
    const isScrollingDown = scrollTop > 0;

    // Add or remove the CSS class based on the scroll direction
    if (isScrollingDown) {
      filterSection.classList.add("fixed-filter");
    } else {
      filterSection.classList.remove("fixed-filter");
    }
  };

  const handleItemClick = (itemId) => {
    navigate(`/courses/${itemId}`);
  };

  return (
    <div className="list-container-courses">
      <div className="filter-section" ref={filterRef}>
        <p>Filters</p>
        {/* <label>
          <input type="checkbox" id="checkbox1" />
          Checkbox 1
        </label>
        <label>
          <input type="checkbox" id="checkbox2" />
          Checkbox 2
        </label>
        <label>
          <input type="checkbox" id="checkbox3" />
          Checkbox 3
        </label> */}
        {difficulties.map((difficulty, index) => (
          <label>
          <input type="checkbox" id={index.toString()} />
          {difficulty}
        </label>
        ))

        }
      </div>
      <div className="all-courses-section">
        {courses.map((item, index) => (
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
