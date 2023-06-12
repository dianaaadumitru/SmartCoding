import React, { useEffect, useState } from "react";
import { AiFillSignal } from "react-icons/ai";
import { useNavigate } from "react-router-dom"; 

import './UserProblems.css'
import getUsersSolvedProblems from "services/userService/getUsersSolvedProblems";

function UserProblems() {
    const [problems, setProblems] = useState([]);
    const navigate = useNavigate(); 
    const userId = parseInt(localStorage.getItem('userId'));

    const itemsPerPage = 3;
    const [currentPage, setCurrentPage] = useState(1);

    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const paginatedItems = problems.slice(startIndex, endIndex);

    const totalPages = Math.ceil(problems.length / itemsPerPage);

    const goToPage = (page) => {
        setCurrentPage(page);
    };

    const getProblems = async () => {
        const result = await getUsersSolvedProblems(userId);
        setProblems(result)
    }

    useEffect(() => {
        getProblems()
    }, [])

    const handleItemClick = (itemId) => {
        navigate(`/auth/solvedProblems/${itemId}`); 
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
                    <div 
                    key={item.problemId} 
                    onClick={() => handleItemClick(item.problemId)}
                    className={`list-item ${item.percentage == 100 ? "completed" : ""}`}
                    >
                        <div className="list-item-header">Problem</div>
                        <h3 className="list-item-heading">{item.problemName}</h3>
                        {/* <p className="list-item-description">{item.problemDescription}</p> */}
                        <div className="list-item-footer"><AiFillSignal />{item.problemDifficulty}</div>
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

export default UserProblems;