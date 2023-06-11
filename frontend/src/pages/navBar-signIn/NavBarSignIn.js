import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import getUserById from 'services/userService/getUserById';

function NavBarSignIn() {
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

  return (
    <nav className="nav-bar">
      <ul className="nav-list">
        <li className="nav-item"><a href="/#explore">Explore</a></li>
        <li className="nav-item"><a href="/#developer">Developer</a></li>
      </ul>
    </nav>
  );
}

export default NavBarSignIn;