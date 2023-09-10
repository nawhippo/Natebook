import React from 'react';

const AcceptFriendRequestButton = ({ friendId, removeRequest }) => {
  const handleClick = () => {
    fetch(`/api/friendreqs/${friendId}/acceptFriendRequest`,{
      method: 'PUT',
    })
    .then(response => {
      if(!response.ok){
        throw new Error("API call failed");
      }
      removeRequest(friendId);
    });
  };

  return <button onClick={handleClick}>Accept Friend Request</button>;
};

export default AcceptFriendRequestButton;