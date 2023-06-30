import { Navigate, Route, Routes } from 'react-router-dom';
import CreateAccountPage from "../pages/createAccountPage/CreateAccountPage";
import HomePage from "../pages/homePage/HomePage";
import React, { useEffect, useState } from 'react';
import SignInPage from '../pages/signInPage/SignInPage';
import MainPage from '../pages/mainPage/MainPage';
import CoursePage from '../pages/coursePage/CoursePage';
import ExploreCourses from '../pages/exploreCourses/ExploreCourses';
import ExploreProblems from 'pages/exploreProblems/ExploreProblems';
import CoursePageAuth from 'pages/coursePageAuth/CoursePageAuth';
import LessonPage from 'pages/lessonPage/LessonPage';
import ProblemPageAuth from 'pages/problemPageAuth/ProblemPageAuth';
import ProblemPage from 'pages/problemPage/ProblemPage';
import MyProfile from 'pages/myProfilePage/MyProfile';
import SolvedProblemPage from 'pages/solvedProblemPage/SolvedProblemPage';
import ChangePswdPage from 'changePswdPage/ChangePswdPage';

const PrivateRoute = ({ element: Component, ...rest }) => {
    const isLoggedIn = localStorage.getItem('isLoggedIn');

    return isLoggedIn ? <Component {...rest} /> : <Navigate to="/signin" />;
};

const RoutesComponent = () => {
    return (
        <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/createAccount" element={<CreateAccountPage />} />
            <Route path="/signin" element={<SignInPage />} />
            <Route path="/problems/:problemId" element={<ProblemPage />} />
            <Route path="/courses/:courseId" element={<CoursePage />} />
            <Route path="/mainpage" element={<PrivateRoute element={MainPage} />} />
            <Route path="/auth/courses/:courseId" element={<PrivateRoute element={CoursePageAuth} />} />
            <Route path="/courses" element={<PrivateRoute element={ExploreCourses} />} />
            <Route path="/problems" element={<PrivateRoute element={ExploreProblems} />} />
            <Route path="/auth/lessons/:lessonId" element={<PrivateRoute element={LessonPage} />} />
            <Route path="/auth/problems/:problemId" element={<PrivateRoute element={ProblemPageAuth} />} />
            <Route path="/auth/solvedProblems/:problemId" element={<PrivateRoute element={SolvedProblemPage} />} />
            <Route path="/auth/myProfile/:userId" element={<PrivateRoute element={MyProfile} />} />
            <Route path="/auth/myProfile/:userId/changePassword" element={<PrivateRoute element={ChangePswdPage} />} />
        </Routes>
    );
};


export default RoutesComponent;
