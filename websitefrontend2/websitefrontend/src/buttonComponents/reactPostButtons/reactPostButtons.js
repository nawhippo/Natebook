import React, { useState } from 'react';

const ReactionButtons = ({ user, postId }) => {
  const [reactionState, setReactionState] = useState('None'); // Possible states: 'None', 'Like', 'Dislike'

  const updateReactionOnServer = async (reactorId, posterId, postId, action) => {
    const url = `/post/${reactorId}/${posterId}/${postId}/updateReactionPost`;
    const response = await fetch(url, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ action })
    });

    if (response.ok) {
      const updatedPost = await response.json();
      // Do something with updatedPost if needed
      return true;
    } else {
      return false;
    }
  };

  const handleButtonClick = async (newReaction) => {
    const isSuccess = await updateReactionOnServer(user.appUserID, postId, user.appUserID, newReaction);
    if (isSuccess) {
      if (newReaction === reactionState) {
        setReactionState('None');
      } else {
        setReactionState(newReaction);
      }
    }
  };

  return (
    <div>
      <button onClick={() => handleButtonClick('Like')}>Like</button>
      <button onClick={() => handleButtonClick('Dislike')}>Dislike</button>
    </div>
  );
};

export default ReactionButtons;