import React from 'react';
import { Link, useHistory } from 'react-router-dom';

const Banner = () => {
  const history = useHistory();

  const handleLinkClick = (url) => {
    //navigate to the specified URL, causing a page refresh in a stupid-roundabout way. 
    history.push(url);
  };

  return (
    <div className="banner">
      {/* Add your banner content here */}
      <h1>Welcome to Our Website!</h1>
      <p>Enjoy your stay!</p>
      {/* Links should be wrapped within a Router component */}
      <button onClick={() => handleLinkClick('/getAllFriends')}>View Friend</button>
      <br />
      <button onClick={() => handleLinkClick('/getAllPosts')}>View All of Your Posts</button>
      <br />
      <button onClick={() => handleLinkClick('/getAllMessages')}>View Messages</button>
      <br />
      <button onClick={() => handleLinkClick('/about')}>About</button>
      <br />
      <button onClick={() => handleLinkClick('/home')}>Home</button>
    </div>
  );
};

export default Banner;