import React, {useState} from 'react';
import {useUserContext} from '../../pages/usercontext/UserContext';
import ThumbUpOffAltIcon from "@mui/icons-material/ThumbUpOffAlt";
import ThumbDownOffAltIcon from "@mui/icons-material/ThumbDownOffAlt";
import './reactPostButtons.css';
import {fetchWithJWT} from "../../utility/fetchInterceptor";
const ReactionButtons = ({ postId, updateLikesDislikes }) => {
  const [reactionState, setReactionState] = useState('None');
  const { user } = useUserContext();
  const [isRateLimited, setIsRateLimited] = useState(false);
  const updateReactionOnServer = async (postId, action) => {
    const url = `/api/post/${postId}/${user.appUserID}/updateReactionPost`;
    const response = await fetchWithJWT(url, {
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
    if (isRateLimited) return;
    setIsRateLimited(true);
    const updatedPost = await updateReactionOnServer(postId, newReaction);
    if (updatedPost) {
      if (newReaction === reactionState) {
        setReactionState('None');
      } else {
        setReactionState(newReaction);
      }
      updateLikesDislikes(updatedPost.likesCount, updatedPost.dislikesCount);
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