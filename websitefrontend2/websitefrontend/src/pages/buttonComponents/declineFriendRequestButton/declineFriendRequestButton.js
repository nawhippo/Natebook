import React from 'react';

const DeclineFriendRequestButton = ({ friendId, removeRequest }) => {
  const handleClick = () => {
    fetch(`/api/friendreqs/${friendId}/declineFriendRequest`,{
      method: 'PUT',
    })
    .then(response => {
      if(!response.ok){
        throw new Error("API call failed");
      }
      removeRequest(friendId);
    });
  };

  return <button onClick={handleClick}>Decline Friend Request</button>;
};

export default DeclineFriendRequestButton;