import React from 'react';
import {useUserContext} from '../../pages/usercontext/UserContext';
import {fetchWithJWT} from "../../utility/fetchInterceptor";
import {getRandomColor} from "../../FunSFX/randomColorGenerator";
import RemoveIcon from '@mui/icons-material/Remove';
const DeclineFriendRequestButton = ({ friendId, triggerFetch  }) => {
  const { user } = useUserContext();

  const buttonStyle = {
    backgroundColor: user && user.backgroundColor ? user.backgroundColor : getRandomColor(),
    color: '#FFFFFF',
    cursor: 'pointer',
    outline: "3px solid black",
    width: '125px',
    height: '70px',
    borderRadius: '30px',
    transform: 'TranslateY(-10px)',
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

  return <RemoveIcon style={{...buttonStyle}} onClick={handleClick}>Decline Friend Request</RemoveIcon>;
};

export default DeclineFriendRequestButton;