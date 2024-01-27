import React from 'react';
import { useUserContext } from '../../pages/usercontext/UserContext';
import {fetchWithJWT} from "../../utility/fetchInterceptor";
import {getRandomColor} from "../../FunSFX/randomColorGenerator";
import {Add} from "@mui/icons-material";
const AcceptFriendRequestButton = ({ friendId, triggerFetch }) => {
  const { user } = useUserContext();
  const buttonStyle = {
    backgroundColor: user && user.backgroundColor ? user.backgroundColor : getRandomColor(),
    color: '#FFFFFF',
    cursor: 'pointer',
    outline: "3px solid black",
    width: '100px',
    height: '50px',
    borderRadius: '30px',
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

  return <Add style={{...buttonStyle}}onClick={handleClick}  className='button-common'>Accept Friend Request</Add>;
};

export default AcceptFriendRequestButton;