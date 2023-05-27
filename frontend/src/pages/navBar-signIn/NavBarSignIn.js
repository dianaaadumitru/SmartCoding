import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import getUserById from 'services/userService/getUserById';
import Logout from 'services/authController/Logout';

function NavBarSignIn(props) {
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

  const handleLogoutClick = async() => {
    const result = await Logout();
    if (result.status == 200) {
      localStorage.clear();
      navigate(`/`);
    }

  }

  return (
    <nav className="nav-bar">
      <ul className="nav-list">
        <li className="nav-item"><a href="/mainpage">Explore</a></li>
        <li className="nav-item"><a href="/#developer">Developer</a></li>
      </ul>
    </nav>
  );
}

export default NavBarSignIn;