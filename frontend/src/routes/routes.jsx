import { Route, Routes as ReactRoutes } from 'react-router-dom';

import CreateAccountPage from "../pages/createAccountPage/CreateAccountPage";
import HomePage from "../pages/homePage/HomePage";
import React from 'react';
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

const Routes = () => {
    return (
        <ReactRoutes>
            <Route path="/" element={<HomePage />} />
            <Route path="/createAccount" element={<CreateAccountPage />} />
            <Route path="/signin" element={<SignInPage />} />
            <Route path="/mainpage" element={<MainPage />} />
            <Route path="/courses/:courseId" element={<CoursePage />} />
            <Route path="auth/courses/:courseId" element={<CoursePageAuth />} />
            <Route path="/courses" element={<ExploreCourses />} />
            <Route path="/problems" element={<ExploreProblems />} />
            <Route path="/auth/lessons/:lessonId" element={<LessonPage />} />
            <Route path="/auth/problems/:problemId" element={<ProblemPageAuth />} />
            <Route path="/problems/:problemId" element={<ProblemPage />} />
            <Route path="/auth/solvedProblems/:problemId" element={<SolvedProblemPage />} />
            <Route path="/auth/myProfile/:userId" element={<MyProfile />} />

        </ReactRoutes>
    )
}

export default Routes;