import React from 'react';
import './HomePageNavBar.css';

function HomePageNavBar({ onExploreClick, onDeveloperClick }) {
  return (
    <div>
      <nav>
        <ul>
          <li onClick={onExploreClick}>Explore</li>
          <li onClick={onDeveloperClick}>Developer</li>
          <li><a href="/signin">Sign in</a></li>
        </ul>
      </nav>
    </div>
  );
}

export default HomePageNavBar;
