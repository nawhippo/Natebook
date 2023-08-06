import React from 'react';
import { Link, useHistory } from 'react-router-dom';
import './banner.css'; // Import the stylesheet

const Banner = () => {
  const history = useHistory();

  const handleLinkClick = (url) => {
    //navigate to the specified URL, causing a page refresh in a stupid-roundabout way. 
    history.push(url);
  };

  return (
    <div className="banner">
      {/* Add your banner content here */}
      <h1>NateBook</h1>
      {/* Links should be wrapped within a Router component */}
      <button onClick={() => handleLinkClick('/getAllFriends')}>View Friends List</button>
      <button onClick={() => handleLinkClick('/getAllPosts')}>View Posts</button>
      <button onClick={() => handleLinkClick('/getFriendRequests')}>View Friend Requests</button>
      <button onClick={() => handleLinkClick('/getAllMessages')}>View Messages</button>
      <button onClick={() => handleLinkClick('/about')}>About</button>
      <button onClick={() => handleLinkClick('/home')}>Home</button>
      <button onClick={() => handleLinkClick('/login')}>Login</button>
      <button onClick={() => handleLinkClick('/createAccount')}>Create Account</button>
      <button onClick={() => handleLinkClick('/createPost')}>Create Post</button>
      <button onClick={() => handleLinkClick('/createMessage')}>Send Message</button>
      <br></br>
      <button onClick={() => handleLinkClick('/addFriend')}>Add Friend</button>
    </div>
  );
};

export default Banner;