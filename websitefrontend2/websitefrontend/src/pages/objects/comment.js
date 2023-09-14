import React, { useState } from 'react';
import { DeleteCommentButton } from '../../buttonComponents/deleteCommentButton/deleteCommentButton';
import ReactionButtons from '../../buttonComponents/reactCommentButtons/reactCommentButtons';

const Comment = ({ comment, user, postId, posterId, fetchData }) => {
  const [localLikesCount, setLocalLikesCount] = useState(comment.likesCount);
  const [localDislikesCount, setLocalDislikesCount] = useState(comment.dislikesCount);

  const updateLocalLikesDislikes = (newLikes, newDislikes) => {
    setLocalLikesCount(newLikes);
    setLocalDislikesCount(newDislikes);
  };

  return (
    <div key={comment.id} className='comment'>
      <p>{comment.content}</p>
      <p>By: {comment.commenterusername}</p>
      <ReactionButtons 
        user={user} 
        postId={postId} 
        posterId={posterId} 
        commentId={comment.id} 
        updateCommentLikesDislikes={updateLocalLikesDislikes}
      />

      {/*both the commenter and the poster can delete the comment*/}
      {user && (comment.commenterusername === user.username || posterId === user.appUserID) && 
        <DeleteCommentButton 
          commenterusername={user.username} 
          postId={postId} 
          commentId={comment.id} 
          targetUsername={comment.commenterusername} 
          fetchData={fetchData} 
        />
      }
    </div>
  );
};

export default Comment;