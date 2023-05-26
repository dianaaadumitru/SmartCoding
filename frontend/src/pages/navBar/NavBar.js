import React, { useEffect, useState } from 'react';
import './NavBar.css';
import { AiOutlineLogout, AiOutlineUser } from "react-icons/ai";
import { useNavigate } from 'react-router-dom';
import getUserById from 'services/userService/getUserById';

function NavBar(props) {
  const navigate = useNavigate();
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const [userId, setUserId] = useState(0);
  const [user, setUser] = useState({
    userId: userId,
    firstName: "",
    lastName: "",
    username: "",
    email: ""
  })

  const getUserId = () => {
    setUserId(parseInt(localStorage.getItem('userId')));
  }

  useEffect(() => {
    getUserId();
  }, [])

  const userById = async() => {
    const id = parseInt(localStorage.getItem('userId'));
    const result = await getUserById(id);
    setUser(result);
  } 

  useEffect(() => {
    userById();
  }, [userId])

  function handleListItemClick() {
    props.targetRef.current.scrollIntoView({ behavior: 'smooth' });
  }

  function handleProfileHover() {
    setIsDropdownOpen(true);
  }

  function handleProfileLeave() {
    setIsDropdownOpen(false);
  }
  const handleUsernameClick = () => navigate(`/auth/myProfile/${userId}`);

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
            <button onClick={handleUsernameClick}>{user.username}</button>
            <hr className="dropdown-line" />
            <button onClick={props.onLogout}><AiOutlineLogout /> Logout</button>
          </div>
        )}
      </div>
    </nav>
  );
}

export default NavBar;