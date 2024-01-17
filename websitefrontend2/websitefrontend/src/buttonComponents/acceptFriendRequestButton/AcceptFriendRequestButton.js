import React from 'react';
import { useUserContext } from '../../pages/usercontext/UserContext';
import {fetchWithJWT} from "../../utility/fetchInterceptor";
import {getRandomColor} from "../../FunSFX/randomColorGenerator";
const AcceptFriendRequestButton = ({ friendId, triggerFetch }) => {
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
    if (user) {
      fetchWithJWT(`/api/friendreqs/${user.appUserID}/acceptFriendRequest/${friendId}`,{
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

  return <button style={{...buttonStyle}}onClick={handleClick}  className='button-common'>Accept Friend Request</button>;
};

export default AcceptFriendRequestButton;