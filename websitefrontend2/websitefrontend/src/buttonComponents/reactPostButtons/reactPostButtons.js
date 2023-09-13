import React, { useState } from 'react';
const ReactionButtons = ({ user, posterId, postId, updateLikesDislikes }) => {
  const [reactionState, setReactionState] = useState('None');

  const updateReactionOnServer = async (reactorId, posterId, postId, action) => {
    const url = `/api/post/${reactorId}/${posterId}/${postId}/updateReactionPost`;
    const response = await fetch(url, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ action })
    });

    if (response.ok) {
      const updatedPost = await response.json();
      return updatedPost;
    } else {
      return null;
    }
  };

  const handleButtonClick = async (newReaction) => {
    const updatedPost = await updateReactionOnServer(user.appUserID, posterId, postId, newReaction);
    if (updatedPost) {
      if (newReaction === reactionState) {
        setReactionState('None');
      } else {
        setReactionState(newReaction);
      }

      updateLikesDislikes(updatedPost.likesCount, updatedPost.dislikesCount); // Update likes and dislikes in Post component
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