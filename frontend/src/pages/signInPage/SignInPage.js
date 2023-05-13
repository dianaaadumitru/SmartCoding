import NavBar from '../navBar/NavBar';
import { useNavigate } from 'react-router-dom';
import React from "react";
import './SignInPage.css'

function SignInPage() {
    const navigate = useNavigate()

    const handleSignin = async() => {
        navigate('/createAccount')
    }
    return (
        <div>
            <NavBar />
            <div className='square'>
                <div className="content">
                    <h2>Smart coding</h2>
                    <input type="text" placeholder="Username or E-mail" />
                    <input type="password" placeholder="Password" />
                    <button className='signin-button' onClick={() => handleSignin()}>Sign In</button>
                </div>
                <a className="bottom-right-text" href='/createAccount'>Sign Up</a>
            </div>
        </div>
    )
}

export default SignInPage;
