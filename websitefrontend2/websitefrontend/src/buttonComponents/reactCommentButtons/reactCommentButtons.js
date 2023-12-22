import React, {useState} from 'react';
import ThumbUpOffAltIcon from "@mui/icons-material/ThumbUpOffAlt";
import ThumbDownOffAltIcon from "@mui/icons-material/ThumbDownOffAlt";
import {useUserContext} from "../../pages/usercontext/UserContext";
import './reactCommentButtons.css';

const ReactionButtons = ({ commentId, updateLikesDislikes }) => {
  const [reactionState, setReactionState] = useState('None');
  const { user } = useUserContext();
  const [isRateLimited, setIsRateLimited] = useState(false);

  const updateReactionOnServer = async (commentId, action) => {
    const url = `/api/comment/${commentId}/${user.appUserID}/updateReactionComment`;
    const response = await fetch(url, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ action })
    });

    if (response.ok) {
      const updatedComment = await response.json();
      return updatedComment;
    } else {
      return null;
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
      updateLikesDislikes(updatedComment.likesCount, updatedComment.dislikesCount);
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
      </div>
  );
};

export default ReactionButtons;