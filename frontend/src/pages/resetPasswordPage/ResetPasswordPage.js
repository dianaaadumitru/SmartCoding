import { useNavigate } from 'react-router-dom';
import React, { useState } from "react";
import NavBarSignIn from 'pages/navBar-signIn/NavBarSignIn';
import forgotPassword from 'services/userService/forgotPassword';
import sendMail from 'services/userService/sendMail';
import Modal from "react-modal";

function ResetPasswordPage() {
    const navigate = useNavigate()
    const [email, setEmail] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [isPopupOpen, setIsPopupOpen] = useState(false);


    const popUpComponent = () => {
        return (
            <Modal
                isOpen={isPopupOpen}
                onRequestClose={() => setIsPopupOpen(false)}
                contentLabel="Password Sent"
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
                        outline: "none",
                        display: "flex",
                        flexDirection: "column",
                        alignItems: "center",
                    }
                }}
            >
                <h1>Password Sent!</h1>
                <p>We just sent a message to the email you provided with a new password. Please check your inbox and also the spam section</p>
                <button
                    className="button"
                    style={{ margin: "10px 0", padding: "8px 16px", cursor: "pointer" }}
                    onClick={() => { setIsPopupOpen(false); navigate(`/signin`); }}
                >
                    Close
                </button>
            </Modal>
        );
    };

    const restPswd = async () => {
        if (!email) {
            setErrorMessage('Please enter your username or email.');
            return;
        }

        try {
            const result = await forgotPassword(email);
            const msgBody = "Password Reset Instructions \n This is your new password: \n" + result.data + "\n Login into your account using it and then change it from the app. \n Have a nice day!";
            const subject = "Reset password instructions";
            await sendMail(email, msgBody, subject);
            setIsPopupOpen(true);
        } catch (error) {
            console.log(error)
            setErrorMessage('This email is not linked to any user.');
        }
    }

    return (
        <div className='page-section'>
            <NavBarSignIn />
            <div className='square'>
                <div className="content">
                    <h2>Request Password Reset</h2>
                    {errorMessage && <p className='error-message'>{errorMessage}</p>}
                    <input type="text" placeholder="E-mail" onChange={(e) => setEmail(e.target.value)} />
                    <button className='signin-button' onClick={() => restPswd()}>Request Password Reset</button>
                </div>
            </div>
            {popUpComponent()}
        </div>
    )
}

export default ResetPasswordPage;
