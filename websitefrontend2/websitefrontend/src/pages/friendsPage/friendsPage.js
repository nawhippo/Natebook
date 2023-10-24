import React from 'react';
import './allfriends.css'
import GetAllFriendRequests from "./helperComponents/getAllFriendRequests"
import GetAllFriends from "./helperComponents/getAllFriends"
const FriendsPage = () => {
  return (
    <div>
      <h1>Friends Page</h1>
      <div className="friends-container">
      <GetAllFriends />
      <GetAllFriendRequests />
      </div>
    </div>
  );
};

export default FriendsPage;
