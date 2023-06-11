import React from "react";
import { Route, Navigate } from "react-router-dom";

const PrivateRoute = ({ children: Component, ...rest }) => {
  const isLoggedIn = localStorage.getItem("isLoggedIn");

  return (
    <Route
      {...rest}
      element={
        isLoggedIn ? (
          <Component {...rest} />
        ) : (
          <Navigate to="/signin" replace={true} />
        )
      }
    />
  );
};

export default PrivateRoute;
