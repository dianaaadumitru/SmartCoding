import React from 'react';
import './NavBar.css';
import { AiOutlineUser } from "react-icons/ai";

function NavBar(props) {
  function handleListItemClick() {
    props.targetRef.current.scrollIntoView({ behavior: 'smooth' });
  }

  return (
    <nav className="nav-bar">
      <ul className="nav-list">
        <li className="nav-item"><a href="/">Home</a></li>
        <li className="nav-item" onClick={handleListItemClick}>Explore</li>
        <li className="nav-item"><a href="#">Developer</a></li>
      </ul>
      <div className="profile-button">
        <button><AiOutlineUser /> My Profile</button>
      </div>
    </nav>
  );
}

export default NavBar;
