import React, {useState} from 'react';
import ThumbUpOffAltIcon from "@mui/icons-material/ThumbUpOffAlt";
import ThumbDownOffAltIcon from "@mui/icons-material/ThumbDownOffAlt";
import {useUserContext} from "../../pages/usercontext/UserContext";
import './reactCommentButtons.css';
import {fetchWithJWT} from "../../utility/fetchInterceptor";

const ReactionButtons = ({ commentId, updateLikesDislikes, likesCount, dislikesCount  }) => {
  const [reactionState, setReactionState] = useState('None');
  const { user } = useUserContext();
  const [isRateLimited, setIsRateLimited] = useState(false);
  const [error, setError] = useState('');
  const updateReactionOnServer = async (commentId, action) => {
    const url = `/api/comment/${commentId}/${user.appUserID}/updateReactionComment`;
    try {
      const response = await fetchWithJWT(url, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ action }),
      });
      const data = await response.json();
      if (response.ok) {
        return data;
      } else {
        setError(data.message || "Failed to update reaction on the server");
        return null;
      }
    } catch (fetchError) {
      setError(fetchError.message);
      return null;
    }
  };

  const handleButtonClick = async (newReaction) => {
    if (!user) return;
    if (isRateLimited) return;
    setIsRateLimited(true);
    const updatedComment = await updateReactionOnServer(commentId, newReaction);
    if (updatedComment) {
      setReactionState(reactionState === newReaction ? 'None' : newReaction);
      updateLikesDislikes(updatedComment.likesCount, updatedComment.dislikesCount);
      setTimeout(() => setIsRateLimited(false), 3000);
    }
  };

  return (
      <div className="reaction-buttons-container">
        <ThumbUpOffAltIcon
            style={{fontSize:'30px'}}
            className={`reaction-icon ${reactionState === 'Like' ? 'active' : ''}`}
            onClick={() => handleButtonClick('Like')}
        />
        <span style={{transform: 'translateY(30px)'}}>{likesCount}</span>
        <ThumbDownOffAltIcon
            style={{fontSize:'30px'}}
            className={`reaction-icon ${reactionState === 'Dislike' ? 'active' : ''}`}
            onClick={() => handleButtonClick('Dislike')}
        />
        <span style={{transform: 'translateY(30px)'}}>{dislikesCount}</span>
      </div>
  );
};

export default ReactionButtons;