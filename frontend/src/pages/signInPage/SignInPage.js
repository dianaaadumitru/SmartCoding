import { useNavigate } from 'react-router-dom';
import React, { useState } from "react";
import './SignInPage.css'
import signIn from '../../services/authController/SignIn';
import NavBarSignIn from 'pages/navBar-signIn/NavBarSignIn';
import { AiFillEye, AiFillEyeInvisible } from 'react-icons/ai';

function SignInPage() {
    const navigate = useNavigate()
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [usernameOrEmail, setUsernameOrEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [showPassword, setShowPassword] = useState(false);

    const handleSignin = async () => {
        if (!usernameOrEmail) {
            setErrorMessage('Please enter your username or email.');
            return;
        }

        if (!password) {
            setErrorMessage('Please enter your password.');
            return;
        }

        try {
            const result = await signIn(usernameOrEmail, password);
            if (result.status === 200) {
                setIsLoggedIn(true);
                localStorage.setItem('userId', result.data);
                navigate('/mainpage');
            }
        } catch (error) {
            console.log(error)
            setErrorMessage('Invalid username or password.');
        }
    }

    const togglePasswordVisibility = () => {
        setShowPassword(!showPassword);
    };

    return (
        <div className='page-section'>
            <NavBarSignIn />
            <div className='square'>
                <div className="content">
                    <h2>Smart coding</h2>
                    {errorMessage && <p className='error-message'>{errorMessage}</p>}
                    <input type="text" placeholder="Username or E-mail" onChange={(e) => setUsernameOrEmail(e.target.value)} />
                    <div className="password-wrapper-login">
                        <input
                            type={showPassword ? "text" : "password"}
                            className="password-input password-textbox-login"
                            placeholder="Password"
                            onChange={(e) => setPassword(e.target.value)}
                        />
                        <button className="view-password-button-login" onClick={togglePasswordVisibility}>
                            {showPassword ? <AiFillEye /> : <AiFillEyeInvisible />}
                        </button>
                    </div>
                    <button className='signin-button' onClick={() => handleSignin()}>Sign In</button>
                </div>
                <a className="bottom-left-text" href='/resetPassword'>Forgot password?</a>
                <a className="bottom-right-text" href='/createAccount'>Sign Up</a>
            </div>
        </div>
    )
}

export default SignInPage;
