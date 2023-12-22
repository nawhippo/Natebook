import React, {useState} from 'react';
import {useUserContext} from '../../pages/usercontext/UserContext';

const SendFriendRequestButton = ({ username }) => {
  const { user } = useUserContext();
  const [sent, setSent] = useState(false);

  const handleAddFriendClick = () => {
    fetch(`/api/friendreqs/${user.appUserID}/sendFriendRequestByUsername/${username}`, {
      method: 'PUT'
    }).then(() => setSent(true));
  };

  const buttonStyle = {
    backgroundColor: user && user.backgroundColor ? user.backgroundColor : 'grey',
    color: '#FFFFFF',
    border: '4px solid black',
  };
  return (
      <>
        {sent ? 'Friend request sent!' : <button onClick={handleAddFriendClick}   className='button-common' style={buttonStyle}>Add Friend</button>}
      </>
  );
};

export default SendFriendRequestButton;