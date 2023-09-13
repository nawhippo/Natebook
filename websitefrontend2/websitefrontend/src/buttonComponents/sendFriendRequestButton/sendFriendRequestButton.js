import React from 'react';
import { useUserContext } from '../../pages/usercontext/UserContext';

const SendFriendRequestButton = ({ username }) => {
  const { user } = useUserContext();
  const handleAddFriendClick = () => {
    fetch(`/api/friendreqs/${user.appUserID}/sendFriendRequestByUsername/${username}`, {
    method: 'PUT'
  });
  };
  return <button onClick={handleAddFriendClick}>Add Friend</button>;
};

export default SendFriendRequestButton;