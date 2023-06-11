import React, { useEffect, useState } from "react";
import './TopProblemsAuth.css'
import { AiFillSignal } from "react-icons/ai";
import getTopProblems from "services/problemService/getTopProblems";
import { useNavigate } from "react-router-dom";
import getProblemScoreForAProblemSoledByUser from "services/userService/getProblemScoreForAProblemSoledByUser";

function TopProblemsAuth() {
    const [problems, setProblems] = useState([]);
    const navigate = useNavigate();

    const itemsPerPage = 4;
    const [currentPage, setCurrentPage] = useState(1);
    const userId = parseInt(localStorage.getItem("userId"));
    const [isCompletionFetched, setIsCompletionFetched] = useState(false);

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

    const getProblemScoreForUser = async (problemId) => {
        const result = await getProblemScoreForAProblemSoledByUser(userId, problemId);
        if (result.score === 100)
            return true;
        return false;
    }

    const fetchCompletionStatus = async () => {
        const promises = problems.map((item) => getProblemScoreForUser(item.problemId));
        const completionStatuses = await Promise.all(promises);

        setProblems((prevProblems) => {
            const updatedProblems = prevProblems.map((problem, index) => ({
                ...problem,
                isCompleted: completionStatuses[index],
            }));
            return updatedProblems;
        });
        setIsCompletionFetched(true);
    };

    useEffect(() => {
        if (problems.length > 0 && !isCompletionFetched) {
            fetchCompletionStatus();
        }
    }, [problems, isCompletionFetched]);

    const handleItemClick = (itemId) => {
        navigate(`/auth/problems/${itemId}`);
    };

    return (
        <div className="list-container">
            <div className="pagination">
                {currentPage > 1 && (
                    <button className="pagination-button" onClick={() => goToPage(currentPage - 1)}>
                        {'<'}
                    </button>
                )}
                {paginatedItems.map((item) => {
                    const problem = problems.find((p) => p.problemId === item.problemId);
                    const isCompleted = problem ? problem.isCompleted || false : false;
                    return (
                        <div
                            key={item.problemId}
                            className={`list-item ${isCompleted ? "completed" : ""}`}
                            onClick={() => handleItemClick(item.problemId)}
                        >
                            <div className="list-item-header">Problem</div>
                            <h3 className="list-item-heading">{item.name}</h3>
                            <p className="list-item-description">{item.description}</p>
                            <div className="list-item-footer"><AiFillSignal />{item.difficulty}</div>
                            <div className="list-item-footer">
                                <AiFillSignal />
                                {item.difficulty}
                            </div>
                        </div>
                    );
                })}



                {currentPage < totalPages && (
                    <button className="pagination-button" onClick={() => goToPage(currentPage + 1)}>
                        {'>'}
                    </button>
                )}
            </div>
        </div>
    );
}

export default TopProblemsAuth;