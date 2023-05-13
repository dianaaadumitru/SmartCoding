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
          <input className="input-class" type="text" placeholder="First Name"/>
          <input type="text" placeholder="Last Name" className="input-class" />
          <input type="text" placeholder="Username" className="input-class" />
          <input type="email" placeholder="E-mail Address" className="input-class" />
          <input type="password" placeholder="Password" className="input-class" />
          <input type="password" placeholder="Confirm Password" className="input-class" />
          <button className='signup-button'>Sign Up</button>
          <p>Have an account? <a href='/signin'>Sign in</a></p>
        </div>
      </div>
    </div>
  );
}

export default CreateAccountPage;
