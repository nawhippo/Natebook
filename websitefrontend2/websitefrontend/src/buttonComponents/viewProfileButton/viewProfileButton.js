import React from 'react';
import {useHistory} from 'react-router-dom';
import {useUserContext} from '../../pages/usercontext/UserContext';

const ViewProfileButton = ({ userid }) => {
  const { user } = useUserContext();
  const history = useHistory();

  const buttonStyle = {
    backgroundColor: user && user.backgroundColor ? user.backgroundColor : '#FF6D00',
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