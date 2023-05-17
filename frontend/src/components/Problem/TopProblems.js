import React, { useEffect, useState } from "react";
import './TopProblems.css'
import { AiFillSignal } from "react-icons/ai";
import getTopProblems from "services/problemService/getTopProblems";

function TopProblems() {
    const [problems, setProblems] = useState([]);

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
}

export default TopProblems;