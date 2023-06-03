import React, { useState } from "react";
import { useNavigate } from 'react-router-dom';

import './CreateAccountPage.css';
import signUp from "services/authController/SignUp";

import { AiFillEye, AiFillEyeInvisible } from "react-icons/ai";
import NavBarSignIn from "pages/navBar-signIn/NavBarSignIn";

function CreateAccountPage() {
  const navigate = useNavigate()
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  const [errorMessage, setErrorMessage] = useState('');

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  const toggleConfirmPasswordVisibility = () => {
    setShowConfirmPassword(!showConfirmPassword);
  };

  const handleSignup = async () => {
    console.log(firstName, lastName, username, email, password, confirmPassword);

    if (!firstName) {
      setErrorMessage('Please enter your first name.');
      return;
    }

    if (!lastName) {
      setErrorMessage('Please enter your last name.');
      return;
    }

    if (!username) {
      setErrorMessage('Please enter your username.');
      return;
    }

    if (!email) {
      setErrorMessage('Please enter your email.');
      return;
    }

    if (!password) {
      setErrorMessage('Please enter your password.');
      return;
    }

    const emailRegex = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i;
    if (!emailRegex.test(email)) {
      setErrorMessage('Invalid email format.');
      return;
    }

    const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
    if (!passwordRegex.test(password)) {
      setErrorMessage('Password must have at least 8 characters and consist of at least one letter and at least one number.');
      return;
    }

    if (password != confirmPassword) {
      setErrorMessage('The passwords you entered do not match.');
      return;
    }

    try {
      const result = await signUp(firstName, lastName, username, email, password);
      console.log(result)
      if (result.data === true && result.status === 200) {
        navigate('/signin');
      }
    } catch (error) {
      console.log(error);
      setErrorMessage(error.message);
    }

  }

  return (
    <div className="page-section">
      <NavBarSignIn />
      <div className="rectangle">
        <div className="content">
          <h2>Smart coding</h2>
          {errorMessage && <p className='error-message'>{errorMessage}</p>}
          <input
            className="input-class"
            type="text"
            placeholder="First Name"
            onChange={(e) => setFirstName(e.target.value)}
          />

          <input
            type="text"
            placeholder="Last Name"
            className="input-class"
            onChange={(e) => setLastName(e.target.value)}
          />

          <input
            type="text"
            placeholder="Username"
            className="input-class"
            onChange={(e) => setUsername(e.target.value)}
          />

          <input
            type="email"
            placeholder="E-mail Address"
            className="input-class"
            onChange={(e) => setEmail(e.target.value)}
          />

          <div className="password-wrapper">
            <input
              placeholder="Password"
              className="input-class password-textbox"
              type={showPassword ? "text" : "password"}
              onChange={(e) => setPassword(e.target.value)}
            />
            <button className="view-password-button" onClick={togglePasswordVisibility}>
              {showPassword ? <AiFillEye /> : <AiFillEyeInvisible />}
            </button>
          </div>

          <div className="password-wrapper move-upper">
            <input
              placeholder="Confirm Password"
              className="input-class password-textbox"
              type={showConfirmPassword ? "text" : "password"}
              onChange={(e) => setConfirmPassword(e.target.value)}
            />
            <button className="view-password-button" onClick={toggleConfirmPasswordVisibility}>
              {showConfirmPassword ? <AiFillEye /> : <AiFillEyeInvisible />}
            </button>
          </div>

          <button className='signup-button' onClick={() => handleSignup()}>Sign Up</button>
          <p className="have-an-account-text">Have an account? <a href='/signin'>Sign in</a></p>
        </div>
      </div>
    </div>
  );
}

export default CreateAccountPage;
