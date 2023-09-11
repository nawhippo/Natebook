import React from 'react';

const AddFriendButton = ({ username, isLoading, error }) => {
  const handleAddFriendClick = () => {
    fetch(`/api/friendreqs/sendFriendRequestByUsername/${username}`);

    if (isLoading) {
      return <div>Loading...</div>;
    }

    if (error) {
      return <div>Error: {error}</div>;
    }
  };

  return <button onClick={handleAddFriendClick}>Add Friend</button>;
};

export default AddFriendButton;