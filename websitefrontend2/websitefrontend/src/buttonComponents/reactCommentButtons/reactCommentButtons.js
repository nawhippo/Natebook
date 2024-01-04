import React, {useState} from 'react';
import ThumbUpOffAltIcon from "@mui/icons-material/ThumbUpOffAlt";
import ThumbDownOffAltIcon from "@mui/icons-material/ThumbDownOffAlt";
import {useUserContext} from "../../pages/usercontext/UserContext";
import './reactCommentButtons.css';

const ReactionButtons = ({ commentId, updateLikesDislikes }) => {
  const [reactionState, setReactionState] = useState('None');
  const { user } = useUserContext();
  const [isRateLimited, setIsRateLimited] = useState(false);
  const [error, setError] = useState('');
  const updateReactionOnServer = async (commentId, action) => {
    const url = `/api/comment/${commentId}/${user.appUserID}/updateReactionComment`;
    try {
      const response = await fetch(url, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ action }),
      });
      if (response.ok) {
        updateLikesDislikes();
      } else {
        // Handle error here if needed
        setError("Failed to update reaction on the server");
      }
    } catch (fetchError) {
      // Handle network or other errors here
      setError(error.message);
    }
  };
  const handleButtonClick = async (newReaction) => {
    if (isRateLimited) return;
    setIsRateLimited(true);
    const updatedComment = await updateReactionOnServer(commentId, newReaction);
    if (updatedComment) {
      if (newReaction === reactionState) {
        setReactionState('None');
      } else {
        setReactionState(newReaction);
      }
      updateLikesDislikes();
      setTimeout(() => setIsRateLimited(false), 3000);
    }
  };

  return (
      <div>
        <ThumbUpOffAltIcon
            className={`reaction-icon ${reactionState === 'Like' ? 'active' : ''}`}
            onClick={() => handleButtonClick('Like')}
        />
        <ThumbDownOffAltIcon
            className={`reaction-icon ${reactionState === 'Dislike' ? 'active' : ''}`}
            onClick={() => handleButtonClick('Dislike')}
        />
        <p style={{color:'red'}}>{error}</p>
      </div>
  );
};

export default ReactionButtons;