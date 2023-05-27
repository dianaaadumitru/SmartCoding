import React, { useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import HomePageNavBar from './homePageNavBar/HomePageNavBar';
import './HomePage.css';
import TopProblems from 'components/Problem/TopProblems/TopProblems';
import TopCourses from 'components/Course/TopCourses/TopCourses';
import DeveloperComponent from 'components/Developer/DeveloperComponent';

function HomePage() {
  const navigate = useNavigate();

  const exploreRef = useRef(null);
  const developerRef = useRef(null);

  useEffect(() => {
    if (window.location.hash === '#explore') {
      exploreRef.current.scrollIntoView({ behavior: 'smooth' });
    } else if (window.location.hash === '#developer') {
      developerRef.current.scrollIntoView({ behavior: 'smooth' });
    }
  }, []);

  const handleExploreClick = () => {
    window.location.hash = 'explore';
    exploreRef.current.scrollIntoView({ behavior: 'smooth' });
  };

  const handleDeveloperClick = () => {
    window.location.hash = 'developer';
    developerRef.current.scrollIntoView({ behavior: 'smooth' });
  };

  const handleClick = () => navigate('/createAccount');
  const handleClickExploreCatalog = () => navigate('/signin');

  return (
    <>
      <section className='homepage-section'>
        <HomePageNavBar
          onExploreClick={handleExploreClick}
          onDeveloperClick={handleDeveloperClick}
        />
        <div className='homepage-content-wrapper'>
          <div className='content-block'>
            <div className='center'>
              <h1 className='page-heading'>Smart coding</h1>
              <p className='page-content'>Solve problems, sharpen skills, succeed in programming.</p>
              <button className="create-account-btn" onClick={handleClick}>Create Account</button>
            </div>
          </div>
        </div>
      </section>
      <section className='explore-section'>
        <h2 ref={exploreRef} className='start-learning'>Start learning</h2>
        <p className='top-courses'>Top courses:</p>
        <TopCourses />
        <button className="create-account-btn" onClick={handleClickExploreCatalog}>Explore full catalog</button>
        <p className='top-courses'>Top problems:</p>
        <TopProblems />
        <button className="create-account-btn" onClick={handleClickExploreCatalog}>Explore full catalog</button>
      </section>
      <section className='developer-section'>
        <h2 ref={developerRef} className='start-learning'>Developer</h2>
        <DeveloperComponent />
      </section>
    </>
  );
}

export default HomePage;
