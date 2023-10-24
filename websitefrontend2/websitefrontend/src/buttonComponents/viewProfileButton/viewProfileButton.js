 
import React from 'react';
import { useHistory } from 'react-router-dom';

const ViewProfileButton = ({ userid }) => {
  const history = useHistory();

  const handleClick = () => {
    history.push(`/userProfile/${userid}`);
  };

  return (
    <button onClick={handleClick}>
      View Profile
    </button>
  );
};

export default ViewProfileButton;