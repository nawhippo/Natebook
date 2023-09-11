import React from 'react';
import GetAllFriendRequests from "./helperComponents/getAllFriendRequests"
import GetAllFriends from "./helperComponents/getAllFriends"
const FriendsPage = () => {
  return (
    <div>
      <h1>Friends Page</h1>
      <GetAllFriends />
      <GetAllFriendRequests />
    </div>
  );
};

export default FriendsPage;
