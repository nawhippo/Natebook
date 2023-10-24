import React, { useState } from 'react';
import { useUserContext } from '../../pages/usercontext/UserContext';
const ReactionButtons = ({ postId, updateLikesDislikes }) => {
  const [reactionState, setReactionState] = useState('None');
  const { user } = useUserContext();
  const updateReactionOnServer = async (postId, action) => {
    const url = `/api/post/${postId}/${user.appUserID}/updateReactionPost`;
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
    const updatedPost = await updateReactionOnServer(postId, newReaction);
    if (updatedPost) {
      if (newReaction === reactionState) {
        setReactionState('None');
      } else {
        setReactionState(newReaction);
      }

      updateLikesDislikes(updatedPost.likesCount, updatedPost.dislikesCount); 
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