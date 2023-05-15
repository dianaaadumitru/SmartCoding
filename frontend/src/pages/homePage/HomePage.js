import React, { useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import HomePageNavBar from './homePageNavBar/HomePageNavBar';
import './HomePage.css';
import Course from 'components/Course/Course';

function HomePage() {
  const navigate = useNavigate()

  const targetRef = useRef(null);

  useEffect(() => {
    if (window.location.hash === '#target') {
      targetRef.current.scrollIntoView({ behavior: 'smooth' });
    }
  }, []);

  const handleClick = async () => navigate('/createAccount')

  return (
    <>
      <section className='homepage-section'>
        <HomePageNavBar targetRef={targetRef} />
        <div className='homepage-content-wrapper'>
          <div className='content-block'>
            <div className='center'>
              <h1 className='page-heading'>Smart coding</h1>
              <p className='page-content'>Solve problems, sharpen skills, succeed in programming.</p>
              <button className="create-account-btn" onClick={() => handleClick()}>Create Account</button>
            </div>
          </div>
        </div>
      </section>
      <section className='explore-section'>
        <h2 ref={targetRef} className='start-learning'>Start learning</h2>
        <Course />
        <p>This is the section you want to scroll to.</p>
      </section>
    </>
  )
}

export default HomePage;
