import React, { useEffect, useState } from "react";
import "./LessonPage.css";
import NavBar from "pages/navBar/NavBar";
import { useNavigate, useParams } from "react-router-dom";
import getLessonById from "services/lessonService/getLessonById";
import * as monacoEditor from "monaco-editor";
import getAllProblemsOfALesson from "services/lessonService/getAllProblemsForALesson";

function LessonPage() {
  const navigate = useNavigate();
  const { lessonId } = useParams();
  const [lesson, setLesson] = useState({
    id: lessonId,
    name: "",
    description: "",
    longDescription: "",
    expectedTime: ""
  });

  const [problems, setProblems] = useState([]);

  const [isConditionMet, setIsConditionMet] = useState(false);

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
      theme: "vs",
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
      // Handle the updated value
    });

    return () => {
      // Dispose of the editor when the component unmounts
      editor.dispose();
    };
  }, []);

  const renderText = (someText) => {
    return someText.split("\n").map((line, index) => (
      <p key={index} className="course-description">
        {line}
      </p>
    ));
  };

  const handleClick = (problem) => {
    // Replace this with your condition logic
    // For example, if the problem name contains 'condition', return true
    // return problem.name.toLowerCase().includes("condition");
    setIsConditionMet(!isConditionMet);
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

          {problems.map((problem, index) => (
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
          <div id="editor" className="monaco-editor-wrapper"></div>
          <button className="button" onClick={handleClick}>Run</button>
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
