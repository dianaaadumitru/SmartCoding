import React, { useEffect, useState } from "react";
import './AllCourses.css'
import { AiFillSignal } from "react-icons/ai";
import { useNavigate } from "react-router-dom"; 
import getAllCourses from "services/courseService/getAllCourses";

function AllCourses() {
  const [courses, setCourses] = useState([]);
  const navigate = useNavigate(); 

  const itemsPerPage = 4;

  const getCourses = async () => {
    const result = await getAllCourses();
    setCourses(result)
  }

  useEffect(() => {
    getCourses()
  }, [])

  const handleItemClick = (itemId) => {
    navigate(`/courses/${itemId}`); 
    // navigate('/mainpage')
  };

  return (
    <div className="list-container-courses">
      {/* <h2 className="courses-heading">Available courses: </h2> */}
      <div className="list-container-courses">
        
        {courses.map((item, index) => (
          <div key={item.id} className="list-item-courses" onClick={() => handleItemClick(item.id)}>
            <div className="list-item-header-courses">Course</div>
            <h3 className="list-item-heading-courses">{item.name}</h3>
            <p className="list-item-description-courses">{item.description}</p>
            <div className="list-item-footer-courses"><AiFillSignal />{item.difficulty}</div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default AllCourses;
