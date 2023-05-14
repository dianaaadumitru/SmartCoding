import NavBar from '../navBar/NavBar';
import { useNavigate } from 'react-router-dom';
import React, { useState } from "react";
import './SignInPage.css'
import signIn from '../../services/authController/SignIn';

function SignInPage() {
    const navigate = useNavigate()
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [usernameOrEmail, setUsernameOrEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    const handleSignin = async () => {
        if (!usernameOrEmail) {
            setErrorMessage('Please enter your username or email.');
            return;
        }

        if (!password) {
            setErrorMessage('Please enter your password.');
            return;
        }

        console.log(usernameOrEmail, password);

        try {
            const result = await signIn(usernameOrEmail, password);
            if (result === true) {
                setIsLoggedIn(true);
                navigate('/mainpage');
            }
        } catch (error) {
            console.log(error)
            setErrorMessage('Invalid username or password.');
        }
    }

    return (
        <div>
            <NavBar />
            <div className='square'>
                <div className="content">
                    <h2>Smart coding</h2>
                    {errorMessage && <p className='error-message'>{errorMessage}</p>}
                    <input type="text" placeholder="Username or E-mail" onChange={(e) => setUsernameOrEmail(e.target.value)} />
                    <input type="password" placeholder="Password" onChange={(e) => setPassword(e.target.value)} />
                    <button className='signin-button' onClick={() => handleSignin()}>Sign In</button>
                </div>
                <a className="bottom-right-text" href='/createAccount'>Sign Up</a>
            </div>
        </div>
    )
}

export default SignInPage;
