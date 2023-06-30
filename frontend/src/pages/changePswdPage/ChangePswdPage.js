import { useNavigate, useParams } from 'react-router-dom';
import React, { useState } from "react";
import { AiFillEye, AiFillEyeInvisible } from 'react-icons/ai';
import NavBarRest from 'pages/navBar-restOfApplication/NavBarRest';
import changePassword from 'services/userService/changePassword';
import Modal from "react-modal";


function ChangePswdPage() {
    const { userId } = useParams();
    const navigate = useNavigate()
    const [oldPassword, setOldPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmNewPassword, setConfirmNewPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [showPasswordO, setShowPasswordO] = useState(false);
    const [showPasswordN, setShowPasswordN] = useState(false);
    const [showPasswordC, setShowPasswordC] = useState(false);
    const [isPopupOpen, setIsPopupOpen] = useState(false);


    const popUpComponent = () => {
        return (
            <Modal
                isOpen={isPopupOpen}
                onRequestClose={() => setIsPopupOpen(false)}
                contentLabel="Password updated"
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
                <h1>Password updated!</h1>
                <button
                    className="button"
                    style={{ margin: "10px 0", padding: "8px 16px", cursor: "pointer" }}
                    onClick={() => { setIsPopupOpen(false); navigate(`/auth/myProfile/${userId}`); }}
                >
                    Close
                </button>
            </Modal>
        );
    };


    const handleChangePswd = async () => {
        if (!oldPassword) {
            setErrorMessage('Please enter your old password.');
            return;
        }

        if (!newPassword) {
            setErrorMessage('Please enter the new password.');
            return;
        }

        if (!confirmNewPassword) {
            setErrorMessage('Please confirm your new password.');
            return;
        }

        if (oldPassword == newPassword) {
            setErrorMessage('Old password and new password should not be the same.');
            return;
        }

        const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
        if (!passwordRegex.test(newPassword)) {
            setErrorMessage('Password must have at least 8 characters and consist of at least one letter and at least one number.');
            return;
        }

        if (newPassword != confirmNewPassword) {
            setErrorMessage('The passwords you entered do not match.');
            return;
        }


        try {
            const result = await changePassword(userId, oldPassword, newPassword);
            console.log(result)
            if (result.status === 200) {
                console.log("everything ok")
                setIsPopupOpen(true);
            }
        } catch (error) {
            console.log(error)
            setErrorMessage('Your old password is not correct!');
        }
    }

    const togglePasswordVisibilityO = () => {
        setShowPasswordO(!showPasswordO);
    };

    const togglePasswordVisibilityN = () => {
        setShowPasswordN(!showPasswordN);
    };

    const togglePasswordVisibilityC = () => {
        setShowPasswordC(!showPasswordC);
    };

    return (
        <div className='page-section'>
            <NavBarRest />
            <div className='square'>
                <div className="content">
                    <h2>Change Password</h2>
                    {errorMessage && <p className='error-message'>{errorMessage}</p>}

                    <div className="password-wrapper-login">
                        <input
                            type={showPasswordO ? "text" : "password"}
                            className="password-input password-textbox-login"
                            placeholder="Old password"
                            onChange={(e) => setOldPassword(e.target.value)}
                        />
                        <button className="view-password-button-login" onClick={togglePasswordVisibilityO}>
                            {showPasswordO ? <AiFillEye /> : <AiFillEyeInvisible />}
                        </button>
                    </div>
                    <div className="password-wrapper-login">
                        <input
                            type={showPasswordN ? "text" : "password"}
                            className="password-input password-textbox-login"
                            placeholder="New password"
                            onChange={(e) => setNewPassword(e.target.value)}
                        />
                        <button className="view-password-button-login" onClick={togglePasswordVisibilityN}>
                            {showPasswordN ? <AiFillEye /> : <AiFillEyeInvisible />}
                        </button>
                    </div>
                    <div className="password-wrapper-login">
                        <input
                            type={showPasswordC ? "text" : "password"}
                            className="password-input password-textbox-login"
                            placeholder="Confirm new password"
                            onChange={(e) => setConfirmNewPassword(e.target.value)}
                        />
                        <button className="view-password-button-login" onClick={togglePasswordVisibilityC}>
                            {showPasswordC ? <AiFillEye /> : <AiFillEyeInvisible />}
                        </button>
                    </div>
                    <button className='signin-button' onClick={() => handleChangePswd()}>Update</button>
                </div>
                {isPopupOpen && popUpComponent()}
            </div>
        </div>
    )
}

export default ChangePswdPage;
