import React, { useEffect, useState, useRef } from "react";
import "./AllProblems.css";
import { AiFillSignal } from "react-icons/ai";
import { useNavigate } from "react-router-dom";
import getDifficulties from "services/difficulty/getDifficulties";
import { AiTwotoneFilter } from "react-icons/ai";
import getAllProblems from "services/problemService/getAllProblems";
import getAllProblemsByDifficulties from "services/problemService/getAllProblemsByDifficulty";
import getProblemScoreForAProblemSoledByUser from "services/userService/getProblemScoreForAProblemSoledByUser";

function AllProblems() {
  const [problems, setProblems] = useState([]);
  const [difficulties, setDifficulties] = useState([]);
  const [checkedDifficulties, setCheckedDifficulties] = useState([]);
  const navigate = useNavigate();
  const filterRef = useRef(null);
  const userId = parseInt(localStorage.getItem("userId"));
  const [isCompletionFetched, setIsCompletionFetched] = useState(false);

  const getProblems = async () => {
    const result = await getAllProblems();
    setProblems(result);
  };

  const getAllDifficulties = async () => {
    const result = await getDifficulties();
    setDifficulties(result);
  }

  const getProblemsByDifficulty = async (checkedDifficulties) => {
    if (checkedDifficulties.length === 0) {
      getProblems();
      setIsCompletionFetched(false)
    } else {
      try {
        const result = await getAllProblemsByDifficulties(checkedDifficulties);
        setProblems(result);
        setIsCompletionFetched(false)
      } catch (error) {
        console.error(error);
      }
    }
  };


  useEffect(() => {
    getProblems();
    getAllDifficulties();
  }, []);

  useEffect(() => {
    getProblemsByDifficulty(checkedDifficulties);
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
    navigate(`/auth/problems/${itemId}`);
  };

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
        {problems.map((item) => {
          const isCompleted = item.isCompleted || false;
          const isFiltered = checkedDifficulties.length > 0 && !checkedDifficulties.includes(item.difficulty);
          return (
            <div
              key={item.problemId}
              className={`list-item-courses ${isFiltered ? "" : (isCompleted ? "completed" : "")}`}
              onClick={() => handleItemClick(item.problemId)}
            >
              <div className="list-item-header-courses">Problem</div>
              <h3 className="list-item-heading-courses">{item.name}</h3>
              <p className="list-item-description-courses">{item.description}</p>
              <div className="list-item-footer-courses">
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

export default AllProblems;
