import React, { useEffect, useState } from "react";
import './Course.css';
import getAllCourses from "services/courseService/getAllCourses";
import { AiFillSignal } from "react-icons/ai";

function Course() {

    const [courses, setCourses] = useState([]);

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
        const result = await getAllCourses();
        setCourses(result)
    }

    useEffect(() => {
        getCourses()
      }, [])

    return (
        <div className="list-container">
            <div className="pagination">
                {currentPage > 1 && (
                    <button className="pagination-button" onClick={() => goToPage(currentPage - 1)}>
                        {'<'}
                    </button>
                )}
                {paginatedItems.map((item) => (
                    <div key={item.id} className="list-item">
                        <div className="list-item-header">Course</div>
                        <h3 className="list-item-heading">{item.name}</h3>
                        <p className="list-item-description">{item.description}</p>
                        <div className="list-item-footer"><AiFillSignal />{item.difficulty}</div>
                    </div>
                ))}
                {currentPage < totalPages && (
                    <button className="pagination-button" onClick={() => goToPage(currentPage + 1)}>
                        {'>'}
                    </button>
                )}
            </div>
        </div>
    );
};

export default Course;