import LoginButton from '../buttonComponents/loginButton/loginButton';
import LogoutButton from '../buttonComponents/logoutButton/logoutButton';
import CreateAccount from '../buttonComponents/createAccountButton/createAccountButton';
import FriendReqCounter from './notificationHelpers/friendRequestHelpers';
import { useHistory } from 'react-router-dom/cjs/react-router-dom.min';
import { useUserContext } from '../pages/usercontext/UserContext';
import './banner.css'


const Banner = () => {
  const history = useHistory();
  const { user } = useUserContext();
  const isLoggedIn = !!user;

  const handleLinkClick = (url) => {
    history.push(url);
  };

  return (
    <div className="banner">
      <h1 className="title">NateBook</h1>
      <button className="button-common" onClick={() => handleLinkClick('/AllUsersPage')}>Users</button>
      <button className="button-common" onClick={() => handleLinkClick('/Feed')}>View Feed</button>
      <FriendReqCounter></FriendReqCounter>

      {isLoggedIn ? (
        <div>
        <p>Logged in as : {user.username} </p>
            <button className="button-common" onClick={() => handleLinkClick('/Account')}>Account</button>
            <button className="button-common" onClick={() => handleLinkClick('/Messages')}>View Messages</button>
            <button className="button-common" onClick={() => handleLinkClick('/Friends')}>Friends</button>
        <LogoutButton />
        </div>
      ) : (
        <>
          <LoginButton />
          <CreateAccount />
        </>
      )}
    </div>
  );
};

export default Banner;