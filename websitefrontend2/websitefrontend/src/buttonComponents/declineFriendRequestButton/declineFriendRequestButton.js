import React from 'react';
import { useUserContext } from '../../pages/usercontext/UserContext';
const DeclineFriendRequestButton = ({ friendId, triggerFetch  }) => {
  const { user } = useUserContext();
  const handleClick = () => {
    fetch(`/api/friendreqs/${user.appUserID}/declineFriendRequest/${friendId}`,{
      method: 'PUT',
    })
    .then(response => {
      if(!response.ok){
        throw new Error("API call failed");
      }
      triggerFetch();
    });
  };

  return <button onClick={handleClick}>Decline Friend Request</button>;
};

export default DeclineFriendRequestButton;