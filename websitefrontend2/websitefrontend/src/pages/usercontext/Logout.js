import React from 'react';
import { useHistory } from 'react-router-dom';
import { useUserContext } from './UserContext';
import Cookies from 'js-cookie';
const Logout = () => {
  const { clearUserContext } = useUserContext();
  const history = useHistory();

  const handleLogout = () => {
    clearUserContext(); 
    Cookies.remove('userData');
    history.push('/login'); 
  };

  return (
    <div>
      <h2>Logout</h2>
      <p>Are you sure you want to log out?</p>
      <button onClick={handleLogout}>Logout</button>
    </div>
  );
};

export default Logout;