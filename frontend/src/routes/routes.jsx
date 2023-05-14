import { Route, Routes as ReactRoutes } from 'react-router-dom';

import CreateAccountPage from "../pages/createAccountPage/CreateAccountPage";
import HomePage from "../pages/homePage/HomePage";
import React from 'react';
import SignInPage from '../pages/signInPage/SignInPage';
import MainPage from '../pages/mainPage/MainPage';

const Routes = () => {
    return (
        <ReactRoutes>
            <Route path="/" element={<HomePage />} />
            <Route path="/createAccount" element={<CreateAccountPage />} />
            <Route path="/signin" element={<SignInPage />} />
            <Route path="/mainpage" element={<MainPage />} />
        </ReactRoutes>
    )
}

export default Routes;