import React, { useEffect, useState } from "react";
import NavBar from "pages/navBar/NavBar";
import * as monacoEditor from "monaco-editor";
import Modal from "react-modal";
import './DeveloperComponent.css'

import runCode from "services/jupyterService/runCode";

function DeveloperComponent() {
  const [textToCompile, setTextToCompile] = useState("");
  const [finalResult, setFinalResult] = useState(-1);
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
        const modifiedText = JSON.stringify(value).replace(/"/g, '').replace(/\\n/g, '\n').replace(/\\r/g, '\r');
        setTextToCompile(modifiedText);
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
    const result = await runCode(textToCompile);
    setFinalResult(result);
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

  return (
    <div className="developer-page-container">
      <NavBar />
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
          <p> right column</p>
        </div>
      </div>
      {popUpComponent()}
    </div>
  );
}

export default DeveloperComponent;
