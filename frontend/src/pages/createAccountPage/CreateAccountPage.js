import React from 'react';

import NavBar from '../navBar/NavBar';
import './CreateAccountPage.css';

function CreateAccountPage() {
  return (
    <div>
      <NavBar />
      <div className="rectangle">
        <div className="content">
          <h2>Smart coding</h2>
          <input type="text" placeholder="First Name" className="inputBox" />
          <input type="text" placeholder="last Name" className="inputBox" />
          <input type="text" placeholder="Username" className="inputBox" />
          <input type="email" placeholder="E-mail Address" className="inputBox" />
          <input type="password" placeholder="Password" className="inputBox" />
          <input type="password" placeholder="Confirm Password" className="inputBox" />
          <button>Sign Up</button>
          <p>Have an account? <a href='#'>Sign in</a></p>
        </div>
      </div>
    </div>
  );
}

export default CreateAccountPage;
