import React, { useEffect, useState } from "react";
import "./LessonPage.css";
import Modal from "react-modal";
import { useNavigate, useParams } from "react-router-dom";
import getLessonById from "services/lessonService/getLessonById";
import * as monacoEditor from "monaco-editor";
import getAllProblemsOfALesson from "services/lessonService/getAllProblemsForALesson";
import runCode from "services/jupyterService/runCode";
import addAnswerAndProblemPercentageToStudent from "services/userService/addAnswerAndProblemPercentageToStudent";
import getProblemScoreForAProblemSoledByUser from "services/userService/getProblemScoreForAProblemSoledByUser";
import markLessonAsCompleted from "services/userService/user-lesson/markLessonAsCompleted";
import getCourseLessonByNoLesson from "services/courseService/getCourseLessonByNoLesson";
import NavBarRest from "pages/navBar-restOfApplication/NavBarRest";

function LessonPage() {
  const navigate = useNavigate();
  const [userId, setUserId] = useState(0);
  const [courseId, setCourseId] = useState(0);
  const [maxLength, setMaxLength] = useState(-1);
  const { lessonId } = useParams();
  const [lesson, setLesson] = useState({
    id: lessonId,
    name: "",
    description: "",
    longDescription: "",
    expectedTime: "",
    noLesson: 0
  });

  const [problems, setProblems] = useState([]);

  const [isConditionMet, setIsConditionMet] = useState(false);

  const [textToCompile, setTextToCompile] = useState('');

  const [finalResult, setFinalResult] = useState({
    finalResult: -1,
    printedResult: null,
    pythonCodeStatus: null
  });

  const [currentProblem, setCurrentProblem] = useState({
    problemId: 0,
    name: "",
    description: "",
    difficulty: "",
    valuesType: "",
    valuesToCheckCode: "",
    resultsToCheckCode: "",
    returnType: "",
    noParameters: -1
  });

  const [isLoading, setIsLoading] = useState(false);

  const [isPopupOpen, setIsPopupOpen] = useState(false);

  const getProblems = async () => {
    const result = await getAllProblemsOfALesson(lessonId);
    setProblems(result);
  };

  const getLesson = async () => {
    const result = await getLessonById(lessonId);
    setLesson(result);
  };

  const getProblemResult = async () => {
    const result = await getProblemScoreForAProblemSoledByUser(userId, currentProblem.problemId);
    if (result.score === 100) {
      setIsConditionMet(true);
      await markLessonAsCompleted(userId, lessonId, courseId);
    } else {
      setIsConditionMet(false);
    }
  }

  const getUserId = () => {
    setUserId(parseInt(localStorage.getItem('userId')));
  }

  const getCourseId = () => {
    setCourseId(parseInt(localStorage.getItem('courseId')));
  }

  const getMaxLength = () => {
    setMaxLength(parseInt(localStorage.getItem('lessonsLength')));
  }

  useEffect(() => {
    getLesson();
    getProblems();
    getUserId();
    getCourseId();
    getMaxLength();

    const editor = monacoEditor.editor.create(document.getElementById("editor"), {
      value: "",
      language: "python",
      theme: "vs-dark",
      fontSize: 14,
      automaticLayout: true,
      wordWrap: "on",
      scrollBeyondLastLine: false,
      minimap: {
        enabled: false
      }
    });

    editor.onDidChangeModelContent(() => {
      const value = editor.getValue();
      const modifiedText = JSON.stringify(value).replace(/^"|"$/g, '').replace(/\\n/g, '\n').replace(/\\r/g, '\r').replace(/\\/g, '');
      setTextToCompile(modifiedText);
    });

    return () => {
      editor.dispose();
    };
  }, []);

  useEffect(() => {
    if (problems.length > 0) {
      setCurrentProblem(problems[0]);
    }
    console.log("params: ", problems[0])
  }, [problems]);

  useEffect(() => {
    if (userId !== -1 && currentProblem.problemId !== 0) {
      getProblemResult();
    }
  }, [userId, currentProblem]);


  const renderText = (someText) => {
    return someText.split("\n").map((line, index) => (
      <p key={index} className="course-description">
        {line}
      </p>
    ));
  };

  const handleClick = async () => {
    if (textToCompile.trim() === "") {
      setIsPopupOpen(true);
      return;
    }

    console.log("params: " + currentProblem.noParameters)
    setIsLoading(true);
    const result = await runCode(textToCompile, currentProblem.valuesType, currentProblem.valuesToCheckCode, currentProblem.resultsToCheckCode, currentProblem.returnType, currentProblem.noParameters)

    if (result.printedResult !== null) {
      result.finalResult = 100;
    }

    setFinalResult(result);
    await addAnswerAndProblemPercentageToStudent(userId, currentProblem.problemId, textToCompile, result.finalResult);
    if (result.finalResult == 100 || result.printedResult != null) {
      setIsConditionMet(true);
      await markLessonAsCompleted(userId, lessonId, courseId);
    } else {
      setIsConditionMet(false);
    }
    setIsLoading(false);
  };

  const handleNextProblem = async () => {
    if (lesson.noLesson == maxLength) {
      navigate(`/auth/courses/${courseId}`);

    } else {
      const result = await getCourseLessonByNoLesson(courseId, lesson.noLesson + 1);
      navigate(`/auth/lessons/${result.id}`);
      window.location.reload();
    }
  };

  const handlePreviousProblem = async () => {
    const result = await getCourseLessonByNoLesson(courseId, lesson.noLesson - 1);
    navigate(`/auth/lessons/${result.id}`);
    window.location.reload();
  };

  const popUpComponent = () => {
    return (
      <Modal
        isOpen={isPopupOpen}
        onRequestClose={() => setIsPopupOpen(false)}
        contentLabel="No Code to Compile"
        style={{
          overlay: {
            position: "fixed",
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            backgroundColor: "rgba(0, 0, 0, 0.5)"
          },
          content: {
            position: "absolute",
            top: "50%",
            left: "50%",
            transform: "translate(-50%, -50%)",
            width: "300px",
            padding: "20px",
            background: "white",
            borderRadius: "4px",
            outline: "none"
          }
        }}
      >
        <h1>No code to compile!</h1>
        <p>Please enter your code before running.</p>
        <button className="button" onClick={() => setIsPopupOpen(false)}>Close</button>
      </Modal>
    );
  };

  return (
    <div className="lesson-page-container">
      <NavBarRest />
      <div className="column-container">
        <div className="left-column">
          <h2 className="header-text">Lesson</h2>
          <h2 className="course-name">{lesson.name}</h2>
          {renderText(lesson.description)}
          <div className="line"></div>
          {renderText(lesson.longDescription)}
          <div className="line"></div>
          <h2 className="course-name problems-title">Problems</h2>

          {problems.map((problem) => (
            <React.Fragment key={problem.id}>
              <div className="additional-content">
                <div className="problem-item">
                  <div className="problem-checkbox">
                    <input
                      type="checkbox"
                      checked={isConditionMet}
                      disabled
                    />
                  </div>
                  <div className="problem-info">
                    <p className="problem-name">{problem.name}</p>
                    <p className="problem-description">
                      {renderText(problem.description)}
                    </p>
                  </div>
                </div>
              </div>
            </React.Fragment>
          ))}
        </div>
        <div className="right-column">
          <div className="editor-wrapper">
            <div id="editor" className="monaco-editor-wrapper"></div>
            <div className="button-wrapper">
              {isLoading ? (
                <div className="loading-indicator">
                  <div className="loader"></div>
                </div>
              ) : (
                <>
                  {finalResult.pythonCodeStatus === 'ok' && (
                    <>
                      {finalResult.finalResult !== -1 && finalResult.finalResult !== null && finalResult.printedResult == null && (
                        <p className="button-text">This code works for {finalResult.finalResult}% of cases.</p>
                      )}
                      {finalResult.printedResult !== null && (
                        <p className="button-text">printed code: {finalResult.printedResult}</p>
                      )}
                    </>
                  )}
                  {finalResult.pythonCodeStatus === 'error' && (
                    <p className="button-text" style={{ color: "red" }}>An error occurred during code execution.</p>
                  )}
                  <button className="button" onClick={handleClick}>Run</button>
                </>
              )}
            </div>
          </div>
        </div>
      </div>

      <div className="problem-navigation-buttons">
        <button
          className="navigation-button"
          onClick={handlePreviousProblem}
          disabled={lesson.noLesson === 1}
        >
          &lt;
        </button>
        <button
          className="navigation-button"
          onClick={handleNextProblem}
          disabled={!isConditionMet}
        >
          &gt;
        </button>
      </div>
      {popUpComponent()}
    </div>
  );
}

export default LessonPage;
