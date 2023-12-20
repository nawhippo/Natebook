import React from 'react';
import { useUserContext } from '../../pages/usercontext/UserContext';
const AcceptFriendRequestButton = ({ friendId, triggerFetch }) => {
  const { user } = useUserContext();

  const handleClick = () => {
    if (user) {
      fetch(`/api/friendreqs/${user.appUserID}/acceptFriendRequest/${friendId}`,{
        method: 'PUT',
      })
      .then(response => {
        if(!response.ok){
          throw new Error("API call failed");
        }
        triggerFetch();
      });
    }
  };

  return <button onClick={handleClick}  className='button-common'>Accept Friend Request</button>;
};

export default AcceptFriendRequestButton;