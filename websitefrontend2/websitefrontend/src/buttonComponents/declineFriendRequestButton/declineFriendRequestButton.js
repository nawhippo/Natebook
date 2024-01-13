import React from 'react';
import {useUserContext} from '../../pages/usercontext/UserContext';
import {fetchWithJWT} from "../../utility/fetchInterceptor";
import {getRandomColor} from "../../FunSFX/randomColorGenerator";
const DeclineFriendRequestButton = ({ friendId, triggerFetch  }) => {
  const { user } = useUserContext();

  const buttonStyle = {
    backgroundColor: user && user.backgroundColor ? user.backgroundColor : getRandomColor(),
    color: '#FFFFFF',
    cursor: 'pointer',
    outline: "3px solid black",
    width: '150px',
    height: '50px'
  };

  const handleClick = () => {
    fetchWithJWT(`/api/friendreqs/${user.appUserID}/declineFriendRequest/${friendId}`,{
      method: 'PUT',
    })
    .then(response => {
      if(!response.ok){
        throw new Error("API call failed");
      }
      triggerFetch();
    });
  };

  return <button style={{...buttonStyle}} onClick={handleClick}>Decline Friend Request</button>;
};

export default DeclineFriendRequestButton;