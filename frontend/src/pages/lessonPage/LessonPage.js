import React, { useEffect, useState } from "react";
import "./LessonPage.css";
import NavBar from "pages/navBar/NavBar";
import { useNavigate, useParams } from "react-router-dom";
import getLessonById from "services/lessonService/getLessonById";
import * as monacoEditor from "monaco-editor";
import getAllProblemsOfALesson from "services/lessonService/getAllProblemsForALesson";
import runCode from "services/jupyterService/runCode";

function LessonPage() {
  const navigate = useNavigate();
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

  const [finalResult, setFinalResult] = useState(-1);

  const [currentProblem, setCurrentProblem] = useState({
    id: 0,
    name: "",
    description: "",
    difficulty: "",
    valuesType: "",
    valuesToCheckCode: "",
    resultsToCheckCode: "",
    returnType: ""
  });

  const [isLoading, setIsLoading] = useState(false);

  const getProblems = async () => {
    const result = await getAllProblemsOfALesson(lessonId);
    setProblems(result);
  };

  const getLesson = async () => {
    const result = await getLessonById(lessonId);
    setLesson(result);
  };

  useEffect(() => {
    getLesson();
    getProblems();

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
      const modifiedText = JSON.stringify(value).replace(/"/g, '').replace(/\\n/g, '\n').replace(/\\r/g, '\r');
      setTextToCompile(modifiedText);
    });

    return () => {
      // Dispose of the editor when the component unmounts
      editor.dispose();
    };
  }, []);

  useEffect(() => {
    if (problems.length > 0) {
      setCurrentProblem(problems[0]);
    }
  }, [problems]);

  const renderText = (someText) => {
    return someText.split("\n").map((line, index) => (
      <p key={index} className="course-description">
        {line}
      </p>
    ));
  };

  const handleClick = async () => {
    setIsLoading(true);
    const result = await runCode(textToCompile, currentProblem.valuesType, currentProblem.valuesToCheckCode, currentProblem.resultsToCheckCode)
    setFinalResult(result.finalResult);
    if (result.finalResult == 100) { setIsConditionMet(true); }
    setIsLoading(false);
  };

  const handleNextProblem = () => {

  };

  const handlePreviousProblem = () => {

  };

  return (
    <div className="lesson-page-container">
      <NavBar />
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
                  {finalResult !== -1 && (
                    <p className="button-text">This code works for {finalResult}% of cases.</p>
                  )}
                  <button className="button" onClick={handleClick}>Run</button>
                </>
              )}
            </div>
          </div>
        </div>
      </div>

      <div className="problem-navigation-buttons">
        <button className="navigation-button" onClick={handlePreviousProblem}>
          &lt;
        </button>
        <button className="navigation-button" onClick={handleNextProblem}>
          &gt;
        </button>
      </div>

    </div>
  );
}

export default LessonPage;
