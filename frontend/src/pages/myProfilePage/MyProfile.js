import React, { useEffect, useState } from "react";
import './MyProfile.css';
import { useParams } from "react-router-dom";
import getUserById from "services/userService/getUserById";
import EnrolledCourses from "components/Course/EnrolledCourses/EnrolledCourses";
import editUser from "services/userService/editUser";
import UserProblems from "components/Problem/UsersProblems/UserProblems";
import NavBarRest from "pages/navBar-restOfApplication/NavBarRest";

function MyProfile() {
    const { userId } = useParams();

    const [user, setUser] = useState({
        userId: userId,
        firstName: "",
        lastName: "",
        username: "",
        email: ""
    });

    useEffect(() => {
        userById();
    }, []);

    const userById = async () => {
        const result = await getUserById(userId);
        setUser(result);
    };

    const handleFirstNameChange = (e) => {
        setUser((prevState) => ({ ...prevState, firstName: e.target.value }));
    };

    const handleLastNameChange = (e) => {
        setUser((prevState) => ({ ...prevState, lastName: e.target.value }));
    };

    const handleUsernameChange = (e) => {
        setUser((prevState) => ({ ...prevState, username: e.target.value }));
    };

    const handleEmailChange = (e) => {
        setUser((prevState) => ({ ...prevState, email: e.target.value }));
    };

    const handleSubmit = async () => {
        console.log(user)
        const result = await editUser(userId, user.firstName, user.lastName, user.username, user.email);
        console.log(user);
    };

    return (
        <div className="myProfile-page-container">
            <NavBarRest />
            <div className="content-container-myProfile">
                <div className="left-column-myProfile">
                    <div className="centered-content-myProfile">
                        <h1>Edit profile</h1>
                        <label>First name </label>
                        <input
                            type="text"
                            placeholder={user.firstName}
                            value={user.firstName}
                            onChange={handleFirstNameChange}
                        />
                        <label>Last name </label>
                        <input
                            type="text"
                            placeholder={user.lastName}
                            value={user.lastName}
                            onChange={handleLastNameChange}
                        />
                        <label>Username </label>
                        <input
                            type="text"
                            placeholder={user.username}
                            value={user.username}
                            onChange={handleUsernameChange}
                        />
                        <label>Email </label>
                        <input
                            type="text"
                            placeholder={user.email}
                            value={user.email}
                            onChange={handleEmailChange}
                        />
                        <button className="update-button-myProfile" onClick={handleSubmit}>Submit</button>
                    </div>
                </div>
                <div className="right-column-myProfile">
                    <p className='my-courses-myProfile'>My courses: </p>
                    <EnrolledCourses />
                    <p className='my-courses-myProfile'>Solved Problems: </p>
                    <UserProblems />
                </div>
            </div>
        </div>
    );
}

export default MyProfile;
