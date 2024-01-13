import React from 'react';
import {useHistory} from 'react-router-dom';
import {useUserContext} from '../../pages/usercontext/UserContext';
import {getRandomColor} from "../../FunSFX/randomColorGenerator";

const ViewProfileButton = ({ userid }) => {
  const { user } = useUserContext();
  const history = useHistory();

  const buttonStyle = {
    backgroundColor: user && user.backgroundColor ? user.backgroundColor : getRandomColor(),
    color: 'dark-grey',
  };

  const handleClick = () => {
    history.push(`/userProfile/${userid}`);
  };

  return (
      <button
          className='button-common'
          onClick={handleClick}
          style={buttonStyle}
      >
        View Profile
      </button>
  );
};

export default ViewProfileButton;