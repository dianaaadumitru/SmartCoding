import { Route, Routes as ReactRoutes } from 'react-router-dom';

import CreateAccountPage from "../pages/createAccountPage/CreateAccountPage";
import HomePage from "../pages/homePage/HomePage";
import React from 'react';

const Routes = () => {
    return (
        <ReactRoutes>
            <Route path="/" element={<HomePage />} />
            <Route path="/createAccount" element={<CreateAccountPage />} />
        </ReactRoutes>
    )
}

export default Routes;