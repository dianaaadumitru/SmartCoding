import React, { useEffect, useState } from "react";
import { AiOutlineLogout, AiOutlineUser } from "react-icons/ai";
import { useNavigate } from "react-router-dom";
import getUserById from "services/userService/getUserById";
import Logout from "services/authController/Logout";

function NavBarRest() {
  const navigate = useNavigate();
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const [userId, setUserId] = useState(0);
  const [user, setUser] = useState({
    userId: userId,
    firstName: "",
    lastName: "",
    username: "",
    email: "",
  });

  const getUserId = () => {
    setUserId(parseInt(localStorage.getItem("userId")));
  };

  useEffect(() => {
    getUserId();
  }, []);

  const userById = async () => {
    const id = parseInt(localStorage.getItem("userId"));
    const result = await getUserById(id);
    setUser(result);
  };

  useEffect(() => {
    userById();
  }, [userId]);

  function handleDeveloperClick() {
    navigate("/mainpage#developer");
  }

  function handleProfileHover() {
    setIsDropdownOpen(true);
  }

  function handleProfileLeave() {
    setIsDropdownOpen(false);
  }
  
  function handleExploreClick() {
    navigate("/mainpage#explore");
  }

  const handleUsernameClick = () =>
    navigate(`/auth/myProfile/${userId}`);

  const handleChangePswdClick = () => {
    navigate(`/auth/myProfile/${userId}/changePassword`);
  }

  const handleLogoutClick = async () => {
    const result = await Logout();
    if (result.status === 200) {
      localStorage.clear();
      navigate(`/`);
    }
  };

  return (
    <nav className="nav-bar">
      <ul className="nav-list">
        <li className="nav-item">
          <a href="/mainpage#explore" onClick={handleExploreClick}>
            Explore
          </a>
        </li>
        <li className="nav-item">
          <a href="/mainpage#developer" onClick={handleDeveloperClick}>
            Developer
          </a>
        </li>
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
            <button onClick={handleChangePswdClick}>change password</button>
            <hr className="dropdown-line" />
            <button onClick={handleLogoutClick}>
              <AiOutlineLogout /> Logout
            </button>
          </div>
        )}
      </div>
    </nav>
  );
}

export default NavBarRest;
