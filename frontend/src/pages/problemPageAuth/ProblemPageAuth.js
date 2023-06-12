import React, { useEffect, useState, useRef } from "react";
import { useParams } from "react-router-dom";
import * as monacoEditor from "monaco-editor";
import Modal from "react-modal";

import runCode from "services/jupyterService/runCode";
import addAnswerAndProblemPercentageToStudent from "services/userService/addAnswerAndProblemPercentageToStudent";
import getProblemScoreForAProblemSoledByUser from "services/userService/getProblemScoreForAProblemSoledByUser";
import getProblemById from "services/problemService/getProblemById";
import NavBarRest from "pages/navBar-restOfApplication/NavBarRest";

function ProblemPageAuth() {
  const [userId, setUserId] = useState(0);
  const { problemId } = useParams();
  const [problem, setProblem] = useState({
    problemId: problemId,
    name: "",
    description: "",
    difficulty: "",
    valuesType: "",
    valuesToCheckCode: "",
    resultsToCheckCode: "",
    returnType: ""
  });

  const [isConditionMet, setIsConditionMet] = useState(false);
  const [textToCompile, setTextToCompile] = useState("");
  const [finalResult, setFinalResult] = useState({
    finalResult: -1,
    printedResult: null,
    pythonCodeStatus: null
  });
  const [isLoading, setIsLoading] = useState(false);
  const [isPopupOpen, setIsPopupOpen] = useState(false);
  const [userAnswer, setUserAnswer] = useState("");
  const [isEditorReady, setIsEditorReady] = useState(false); // Add isEditorReady state

  const editorRef = useRef(null);

  const getProblem = async () => {
    const result = await getProblemById(problemId);
    setProblem(result);
  };

  const getProblemResult = async () => {
    const result = await getProblemScoreForAProblemSoledByUser(userId, problemId);
    setUserAnswer(result.answer);
    if (result.score === 100 || result.printedResult != null) {
      setIsConditionMet(true);
    } else {
      setIsConditionMet(false);
    }
  };

  const getUserId = () => {
    setUserId(parseInt(localStorage.getItem("userId")));
  };

  useEffect(() => {
    getProblem();
    getUserId();
  }, []);
  
  useEffect(() => {
    if (userId !== 0 && parseInt(problemId) !== 0) {
      getProblemResult();
      setIsEditorReady(true)
    }
  }, [userId, problemId]);
  
  useEffect(() => {
    if (isEditorReady && editorRef.current) { // Add null check for editorRef.current
      const valueToSet = userAnswer !== "" && userAnswer !== null && userAnswer !== undefined
        ? userAnswer
        : "";
  
      editorRef.current.setValue(valueToSet);
    }
  }, [isEditorReady, userAnswer]);

  useEffect(() => {
    if (isEditorReady) { 
      console.log("user answer: ", userAnswer);
      editorRef.current = monacoEditor.editor.create(
        document.getElementById("editor"),
        {
          // value:  (userAnswer !== "" && userAnswer !== null && userAnswer !== undefined) ? userAnswer: "",
          language: "python",
          theme: "vs-dark",
          fontSize: 14,
          automaticLayout: true,
          wordWrap: "on",
          scrollBeyondLastLine: false,
          minimap: {
            enabled: false
          }
        }
      );

      editorRef.current.onDidChangeModelContent(() => {
        const value = editorRef.current.getValue();
        const modifiedText = JSON.stringify(value)
          .replace(/^"|"$/g, "")
          .replace(/\\n/g, "\n")
          .replace(/\\r/g, "\r")
          .replace(/\\/g, "");
        setTextToCompile(modifiedText);
      });

      return () => {
        editorRef.current.dispose();
      };
    }
  }, [isEditorReady]);


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

    setIsLoading(true);
    const result = await runCode(
      textToCompile,
      problem.valuesType,
      problem.valuesToCheckCode,
      problem.resultsToCheckCode,
      problem.returnType
    );
    setFinalResult(result);
    if (result.printedResult !== null) {
        result.finalResult = 100;
    }
    await addAnswerAndProblemPercentageToStudent(
      userId,
      problem.problemId,
      textToCompile,
      result.finalResult
    );
    if (result.finalResult === 100 || result.printedResult != null) {
        console.log("here")
      setIsConditionMet(true);
    } else {
      setIsConditionMet(false);
    }
    setIsLoading(false);
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
        <button className="button" onClick={() => setIsPopupOpen(false)}>
          Close
        </button>
      </Modal>
    );
  };

  return (
    <div className="lesson-page-container">
      <NavBarRest />
      <div className="column-container">
        <div className="left-column">
          <h2 className="header-text">Problem</h2>
          <h2 className="course-name">{problem.name}</h2>
          <div className="line"></div>
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
                <p className="problem-description">
                  {renderText(problem.description)}
                </p>
              </div>
            </div>
          </div>
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
                  {finalResult.pythonCodeStatus === "ok" && (
                    <>
                      {finalResult.finalResult !== -1 &&
                        finalResult.finalResult !== null && (
                          <p className="button-text">
                            This code works for {finalResult.finalResult}% of
                            cases.
                          </p>
                        )}
                      {finalResult.printedResult !== null && (
                        <p className="button-text">
                          printed code: {finalResult.printedResult}
                        </p>
                      )}
                    </>
                  )}
                  {finalResult.pythonCodeStatus === "error" && (
                    <p className="button-text" style={{ color: "red" }}>
                      An error occurred during code execution.
                    </p>
                  )}
                  <button className="button" onClick={handleClick}>
                    Run
                  </button>
                </>
              )}
            </div>
          </div>
        </div>
      </div>
      {popUpComponent()}
    </div>
  );
}

export default ProblemPageAuth;
