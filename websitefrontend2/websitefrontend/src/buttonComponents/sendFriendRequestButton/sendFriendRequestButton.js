import React, { useState } from 'react';
import { useUserContext } from '../../pages/usercontext/UserContext';

const SendFriendRequestButton = ({ username }) => {
  const { user } = useUserContext();
  const [sent, setSent] = useState(false);

  const handleAddFriendClick = () => {
    fetch(`/api/friendreqs/${user.appUserID}/sendFriendRequestByUsername/${username}`, {
      method: 'PUT'
    }).then(() => setSent(true));
  };

  return (
    <>
      {sent ? 'Friend request sent!' : <button onClick={handleAddFriendClick}>Add Friend</button>}
    </>
  );
};

export default SendFriendRequestButton;