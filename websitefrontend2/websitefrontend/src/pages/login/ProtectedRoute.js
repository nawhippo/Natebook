import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import { useUserContext } from './UserContext';

const ProtectedRoute = ({ component: Component, ...rest }) => {
  const { user } = useUserContext();

  return (
    <Route
      {...rest}
      render={(props) =>
        user ? <Component {...props} /> : <Redirect to="/login" />
      }
    />
  );
};

export default ProtectedRoute;