import React, { useEffect, useState } from "react";
import NavBar from "pages/navBar/NavBar";
import { useNavigate, useParams } from "react-router-dom";
import * as monacoEditor from "monaco-editor";
import getProblemById from "services/problemService/getProblemById";

function ProblemPage() {
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
    })
    const navigate = useNavigate(); 


    const getProblem = async () => {
        const result = await getProblemById(problemId);
        setProblem(result);
    };


    useEffect(() => {
        getProblem();

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
            },
            readOnly: true,
        });

        editor.onDidChangeModelContent(() => {
            
        });

        return () => {
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

    const handleClick = async () => {
        navigate(`/signin`); 

    };

    return (
        <div className="lesson-page-container">
            <NavBar />
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
                        <button className="button" onClick={handleClick}>Login</button>
                    </div>
                </div>
            </div>
        </div>

    );
}

export default ProblemPage;
