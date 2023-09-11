import React from 'react';
import { useHistory } from 'react-router-dom';
import { useUserContext } from '../../pages/usercontext/UserContext';
import Cookies from 'js-cookie';

const LogoutButton = () => {
  const { clearUserContext } = useUserContext();
  const history = useHistory();

  const handleLogout = () => {
    clearUserContext();
    Cookies.remove('userData');
  };

  return (
    <button onClick={handleLogout}>Logout</button>
  );
};

export default LogoutButton;