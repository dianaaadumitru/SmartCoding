import React, { useEffect, useState } from "react";
import NavBar from "pages/navBar/NavBar";
import { useNavigate, useParams } from "react-router-dom";
import * as monacoEditor from "monaco-editor";
import Modal from "react-modal";

import runCode from "services/jupyterService/runCode";
import addAnswerAndProblemPercentageToStudent from "services/userService/addAnswerAndProblemPercentageToStudent";
import getProblemScoreForAProblemSoledByUser from "services/userService/getProblemScoreForAProblemSoledByUser";
import getProblemById from "services/problemService/getProblemById";

function SolvedProblemPage() {
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
        returnType: "", 
        noParameters: 0
    })

    const [isConditionMet, setIsConditionMet] = useState(false);

    const [textToCompile, setTextToCompile] = useState('');

    const [finalResult, setFinalResult] = useState(-1);

    const [givenAnswer, setGivenAnswer] = useState('');

    const [isLoading, setIsLoading] = useState(false);

    const [isPopupOpen, setIsPopupOpen] = useState(false);


    const getProblem = async () => {
        const result = await getProblemById(problemId);
        setProblem(result);
    };

    const getProblemResult = async () => {
        const result = await getProblemScoreForAProblemSoledByUser(userId, problemId);
        setFinalResult(result.score);
        setGivenAnswer(result.answer);
        setTextToCompile(result.answer);
        localStorage.setItem("givenAnswer", result.answer);
        if (result.score === 100) {
            setIsConditionMet(true);
        } else {
            setIsConditionMet(false);
        }
    }

    const getUserId = () => {
        setUserId(parseInt(localStorage.getItem('userId')));
    }

    useEffect(() => {
        getProblem();
        getUserId();

        const storedGivenAnswer = localStorage.getItem("givenAnswer");
        if (storedGivenAnswer) {
            setGivenAnswer(storedGivenAnswer);
        }
    }, []);

    useEffect(() => {
        if (givenAnswer !== '') {
            const editor = monacoEditor.editor.create(document.getElementById("editor"), {
                value: givenAnswer,
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
                console.log("value: ", value)
                const modifiedText = JSON.stringify(value).replace(/"/g, '').replace(/\\n/g, '\n').replace(/\\r/g, '\r');
                setTextToCompile(modifiedText);
            });

            return () => {
                editor.dispose();
            };
        }
    }, [givenAnswer])


    useEffect(() => {
        if (userId !== -1 && parseInt(problemId) !== 0) {
            getProblemResult();

        }
    }, [userId, problem]);

    useEffect(() => {
        // Retrieve the givenAnswer value from local storage when the component mounts
        const storedGivenAnswer = localStorage.getItem("givenAnswer");
        if (storedGivenAnswer) {
            setGivenAnswer(storedGivenAnswer);
        }
    }, []);


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
        const result = await runCode(textToCompile, problem.valuesType, problem.valuesToCheckCode, problem.resultsToCheckCode, problem.noParameters)
        setFinalResult(result.finalResult);
        await addAnswerAndProblemPercentageToStudent(userId, problem.problemId, textToCompile, result.finalResult);
        if (result.finalResult == 100) {
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
                <button className="button" onClick={() => setIsPopupOpen(false)}>Close</button>
            </Modal>
        );
    };

    return (
        <div className="lesson-page-container">
            <NavBar />
            <div className="column-container">
                <div className="left-column">
                    <h2 className="header-text">Problem</h2>
                    <h2 className="course-name">{problem.name}</h2>
                    <p className="problem-description">The solution you provided for this problem works for {finalResult}% of cases.</p>
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
            {popUpComponent()}
        </div>

    );
}

export default SolvedProblemPage;



