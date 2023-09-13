import LoginButton from '../buttonComponents/loginButton/loginButton';
import LogoutButton from '../buttonComponents/logoutButton/logoutButton';
import CreateAccount from '../buttonComponents/createAccountButton/createAccountButton';
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
      <h1>NateBook</h1>
      <button onClick={() => handleLinkClick('/AllUsersPage')}>Users</button>
      <button onClick={() => handleLinkClick('/Account')}>Account</button>
      <button onClick={() => handleLinkClick('/Messages')}>View Messages</button>
      <button onClick={() => handleLinkClick('/Feed')}>View Feed</button>
      <button onClick={() => handleLinkClick('/Friends')}>Friends</button>

      {isLoggedIn ? (
        <div>
        <p>Logged in as : {user.username} </p>
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