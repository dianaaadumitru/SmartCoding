import React from 'react';
import { useNavigate } from 'react-router-dom';
import HomePageNavBar from './homePageNavBar/HomePageNavBar';
import './HomePage.css';

function HomePage() {
  const navigate = useNavigate()

  const handleClick = async () => navigate('/createAccount')

  return (
    <section className='homepage-section'>
      <HomePageNavBar />
      <div className='content-block'>
        <div className='center'>
          <h1 className='page-heading page-heading--small'>Smart coding</h1>
          <p className='page-content page-content--small'>Solve problems, sharpen skills, succeed in programming.</p>
          <button className="create-account-btn" onClick={() => handleClick()}>Create Account</button>
        </div>
      </div>
    </section>
  )
}

export default HomePage;
