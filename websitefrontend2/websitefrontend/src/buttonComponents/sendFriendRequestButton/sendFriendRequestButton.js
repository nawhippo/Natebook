import React, {useState} from 'react';
import {useUserContext} from '../../pages/usercontext/UserContext';
import {fetchWithJWT} from "../../utility/fetchInterceptor";
import {getRandomColor} from "../../FunSFX/randomColorGenerator";
import PersonAddIcon from '@mui/icons-material/PersonAdd';
const SendFriendRequestButton = ({ username }) => {
  const { user } = useUserContext();
  const [sent, setSent] = useState(false);

  const handleAddFriendClick = () => {
    fetchWithJWT(`/api/friendreqs/${user.appUserID}/sendFriendRequestByUsername/${username}`, {
      method: 'PUT'
    }).then(() => setSent(true));
  };

  const buttonStyle = {
    backgroundColor: user && user.backgroundColor ? user.backgroundColor : getRandomColor(),
    color: '#FFFFFF',
    border: '4px solid black',
    padding: '10px',
    borderRadius: '15px',
    width: '25px',
    height: '25px',
  };

  return (
      <>
        {sent ? 'Friend request sent!' : <PersonAddIcon onClick={handleAddFriendClick}   className='button-common' style={buttonStyle}></PersonAddIcon>}
      </>
  );
};

export default SendFriendRequestButton;