import React, { useState } from 'react';
import './NavBar.css';
import { AiOutlineLogout, AiOutlineUser } from "react-icons/ai";

function NavBar(props) {
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);

  function handleListItemClick() {
    props.targetRef.current.scrollIntoView({ behavior: 'smooth' });
  }

  function handleProfileHover() {
    setIsDropdownOpen(true);
  }

  function handleProfileLeave() {
    setIsDropdownOpen(false);
  }

  return (
    <nav className="nav-bar">
      <ul className="nav-list">
        <li className="nav-item"><a href="/mainpage">Explore</a></li>
        <li className="nav-item"><a href="#">Developer</a></li>
      </ul>
      <div
        className="profile-button"
        onMouseEnter={handleProfileHover}
        onMouseLeave={handleProfileLeave}
      >
        <button>
          <AiOutlineUser /> My Profile
        </button>
        {isDropdownOpen && (
          <div className="dropdown-menu">
            <a href="#">username</a>
            <hr className="dropdown-line" />
            <button onClick={props.onLogout}><AiOutlineLogout /> Logout</button>
          </div>
        )}
      </div>
    </nav>
  );
}

export default NavBar;