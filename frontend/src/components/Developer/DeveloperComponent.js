import React, { useEffect, useState } from "react";
import NavBar from "pages/navBar/NavBar";
import * as monacoEditor from "monaco-editor";
import Modal from "react-modal";
import './DeveloperComponent.css'

import runCodeDeveloper from "services/jupyterService/runCodeDeveloper";

function DeveloperComponent() {
    const [textToCompile, setTextToCompile] = useState("");
    const [finalResult, setFinalResult] = useState({
        requestStatus: "",
        codeExecutionResult: {
            printedResult: "",
            returnedResult: "",
            pythonCodeStatus: ""
        }
    });
    const [isLoading, setIsLoading] = useState(false);
    const [isPopupOpen, setIsPopupOpen] = useState(false);

    useEffect(() => {
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
            console.log("text ", modifiedText)
        });

        return () => {
            editor.dispose();
        };
    }, []);

    const handleClick = async () => {
        if (textToCompile.trim() === "") {
            setIsPopupOpen(true);
            return;
        }

        setIsLoading(true);
        console.log("final result before ", finalResult);
        const result = await runCodeDeveloper(textToCompile);
        setFinalResult(result)
        console.log("final result ", result);
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
                        backgroundColor: "rgba(0, 0, 0, 0.5)",
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
                        outline: "none",
                    },
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

    const displayResult = () => {
        const { requestStatus, codeExecutionResult } = finalResult;
        console.log("req status ", finalResult.requestStatus, " code reult ", finalResult.codeExecutionResult)

        if (requestStatus === "ok") {
            const { printedResult, returnedResult, pythonCodeStatus } = codeExecutionResult;

            if (returnedResult !== null) {
                return (
                    <p style={{ color: "black" }}>{`Returned Result: ${returnedResult}`}</p>
                );
            }
            if (printedResult !== null) {
                return (
                    <p style={{ color: "black" }}>{`Printed Result: ${printedResult}`}</p>
                );
            }
            if (printedResult == null && returnedResult == null) {
                return (
                    <p style={{ color: "black" }}>No result to display.</p>
                );
            }
        } else if (requestStatus === "error") {
            return (
                <p style={{ color: "red" }}>An error occurred during code execution.</p>
            );
        } else {
            return null;
        }
    };

    useEffect(() => {
        displayResult();
    }, [finalResult]);

    return (
        <div className="developer-page-container">
            <div className="developer-column-container">
                <div className="developer-left-column">
                    <div className="developer-editor-wrapper">
                        <div id="editor" className="developer-monaco-editor-wrapper"></div>
                        <div className="developer-button-wrapper">
                            {isLoading ? (
                                <div className="developer-loading-indicator">
                                    <div className="developer-loader"></div>
                                </div>
                            ) : (
                                <>
                                    <button className="button" onClick={handleClick}>
                                        Run
                                    </button>
                                </>
                            )}
                        </div>
                    </div>
                </div>

                <div className="developer-right-column">
                    {finalResult.requestStatus != '' && (
                        <>
                            {
                                finalResult.requestStatus == 'DONE' && (
                                    <>
                                        <p style={{ color: "black", fontWeight: "bold" }}>Result: </p>

                                        {
                                            finalResult.codeExecutionResult.pythonCodeStatus == 'ok' ? (
                                                <>
                                                    {finalResult.codeExecutionResult.printedResult != null && (
                                                        <p style={{ color: "black" }}>{`Printed result: ${finalResult.codeExecutionResult.printedResult}`}</p>
                                                    )}
                                                    {finalResult.codeExecutionResult.returnedResult != null && (
                                                        <p style={{ color: "black" }}>{`Returned result: ${finalResult.codeExecutionResult.returnedResult}`}</p>
                                                    )}
                                                </>

                                            ) : (
                                                <p style={{ color: "red" }}>An error occurred during code execution.</p>
                                            )
                                        }
                                    </>
                                )
                            }
                        </>

                    )}

                </div>
            </div>
            {popUpComponent()}
        </div>
    );
}

export default DeveloperComponent;
