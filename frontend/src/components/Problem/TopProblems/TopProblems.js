import React, { useEffect, useState } from "react";
import './TopProblems.css'
import { AiFillSignal } from "react-icons/ai";
import getTopProblems from "services/problemService/getTopProblems";
import { useNavigate } from "react-router-dom"; // Import useNavigate from react-router-dom

function TopProblems() {
    const [problems, setProblems] = useState([]);
    const navigate = useNavigate(); // Create a navigate function

    const itemsPerPage = 4;
    const [currentPage, setCurrentPage] = useState(1);

    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const paginatedItems = problems.slice(startIndex, endIndex);

    const totalPages = Math.ceil(problems.length / itemsPerPage);

    const goToPage = (page) => {
        setCurrentPage(page);
    };

    const getProblems = async () => {
        const result = await getTopProblems();
        setProblems(result)
    }

    useEffect(() => {
        getProblems()
    }, [])

    const handleItemClick = (itemId) => {
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
                        <div className="list-item-header">Problem</div>
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

export default TopProblems;