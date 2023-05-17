// 

import React, { useEffect, useState } from "react";
import './TopCourses.css'
import { AiFillSignal } from "react-icons/ai";
import { useNavigate } from "react-router-dom"; 
import getTopCourses from "services/courseService/getTopCourses";

function TopCourses() {
    const [courses, setCourses] = useState([]);
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

    useEffect(() => {
        getCourses()
    }, [])

    const handleItemClick = (itemId) => {
        // Perform any necessary logic or data manipulation before navigating
        // navigate(`/problem/${itemId}`); // Replace "/problem" with the desired URL of the destination page
        navigate('/mainpage')
    };

    return (
        <div className="list-container">
            <div className="pagination">
                {currentPage > 1 && (
                    <button className="pagination-button" onClick={() => goToPage(currentPage - 1)}>
                        {'<'}
                    </button>
                )}
                {paginatedItems.map((item) => (
                    <div key={item.id} className="list-item" onClick={() => handleItemClick(item.id)}>
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
}

export default TopCourses;