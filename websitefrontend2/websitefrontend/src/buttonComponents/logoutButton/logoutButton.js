import React, {useState} from 'react';
import { useHistory } from 'react-router-dom';
import { useUserContext } from '../../pages/usercontext/UserContext';
import Cookies from 'js-cookie';

const LogoutButton = () => {
  const {user, clearUserContext } = useUserContext();
  const history = useHistory();
  const [error, setError] = useState();
  const [buttonUsed, setButtonUsed] = useState('');
  const handleLogout = () => {
    const fetch = ((`/api/${user.appUserID}/logout`), {
      Method: "PUT"})
      .then(response => {
        if(!response.ok){
          throw new Error('Response was not ok!')
    }
          console.log('Logged out successfully');
          setButtonUsed('Logged out successfully');
        })
    .catch(error => {
    console.log('Logout failed');
    setButtonUsed('Logout failed');
    });
    };

    clearUserContext();
    Cookies.remove('userData');
    history.push('/Feed');

  return(
      <div>
    <button onClick={handleLogout}>Logout</button>
        {buttonUsed && <p>{buttonUsed}</p>}
      </div>
  );
};
export default LogoutButton;