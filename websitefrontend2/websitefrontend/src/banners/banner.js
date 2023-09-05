import React, { useContext }  from 'react';
import { useHistory } from 'react-router-dom';
import './banner.css';
import { useUserContext } from '../pages/usercontext/UserContext';
import { UserContext } from '../pages/usercontext/UserContext';
const Banner = () => {
  const history = useHistory();
  const { user, setUser, clearUserContext } = useUserContext();
  const isLoggedIn = !!user;
  const handleLinkClick = (url) => {
    history.push(url);
  };

  return (
    <div className="banner">
      <h1>NateBook</h1>

      <div className="dropdown">
        <button className="dropbtn">Account Management</button>
        <div className="dropdown-content">
          {isLoggedIn ? (
          <>
          <p onClick={() => handleLinkClick('/logout')}>Logout</p>
          <p onClick={() => handleLinkClick('/accountDetails')}>Account Details</p>
          </>
           ) : ( 
            <>
            <p onClick={() => handleLinkClick('/createAccount')}>Create Account</p>
            <p onClick={() => handleLinkClick('/login')}>Login</p>
            </>
           )}
        </div>
      </div>

      <div className="dropdown">
        <button className="dropbtn">Friends</button>
        <div className="dropdown-content">
          <p onClick={() => handleLinkClick('/getAllFriends')}>View Friends List</p>
          <p onClick={() => handleLinkClick('/getFriendRequests')}>View Friend Requests</p>
          <p onClick={() => handleLinkClick('/sendFriendRequest')}>Send Friend Request</p>
        </div>
      </div>

      <div className="dropdown">
        <button className="dropbtn">Posts</button>
        <div className="dropdown-content">
          <p onClick={() => handleLinkClick('/getAllPosts')}>View Posts</p>
          <p onClick={() => handleLinkClick('/createPost')}>Create Post</p>
        </div>
      </div>

      <button onClick={() => handleLinkClick('/home')}>Home</button>
      <button onClick={() => handleLinkClick('/about')}>About</button>

      <div className="dropdown">
        <button className="dropbtn">Messages</button>
        <div className="dropdown-content">
          <p onClick={() => handleLinkClick('/getAllMessages')}>View Messages</p>
          <p onClick={() => handleLinkClick('/createMessage')}>Send Message</p>
        </div>
      </div>
    </div>
  );
};

export default Banner;