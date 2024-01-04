import React, {useState} from 'react';
import {useHistory} from 'react-router-dom';
import Cookies from 'js-cookie';
import {useUserContext} from "../../pages/usercontext/UserContext";
import LogoutIcon from '@mui/icons-material/Logout';
import {fetchWithJWT} from "../../utility/fetchInterceptor";


const LogoutButton = () => {
  const history = useHistory();
  const [error, setError] = useState('');
  const { clearUserContext } = useUserContext();
  const handleLogout = () => {
    const userData = Cookies.get('userData');
    const userId = userData ? JSON.parse(userData).appUserID : null;

    if (userId) {
      fetchWithJWT(`/api/${userId}/logout`, { method: "POST" })
          .then(response => {
            if (!response.ok) {
              throw new Error('Response was not ok!');
            }
            console.log('Logged out successfully');
            Cookies.remove('userData'); // Remove user data from cookie
              Cookies.remove('jwt');
              history.push('/Feed');
            window.location.reload();
          })
          .catch(error => {
            console.error('Logout failed:', error);
            setError('Logout failed');
          });
    } else {
      console.error('No user data found for logout');
      setError('No user data found');
    }
  };

  return (
        <LogoutIcon className="button-common" style={{ width: '50px', height: '50px', background: 'none', color: 'white' }} onClick={handleLogout}>Logout</LogoutIcon>
  );
};

export default LogoutButton;