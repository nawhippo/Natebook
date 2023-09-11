//this should direct to the users profile page, not make any fetches. 
import React from 'react';
import { useHistory } from 'react-router-dom';

const ViewProfileButton = ({ username }) => {
  const history = useHistory();

  const handleClick = () => {
    history.push(`/userProfile/${username}`);
  };

  return (
    <button onClick={handleClick}>
      View Profile
    </button>
  );
};

export default ViewProfileButton;