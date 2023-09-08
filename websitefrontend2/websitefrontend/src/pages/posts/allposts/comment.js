import React from 'react';

//to shorten posts page.
const Comment = ({ comment, user, postId, handleReaction, handleDeleteComment }) => {

  const handleCommentClick = () => {
    
  }
  return (
    <div key={comment.id} className='comment'>
      <p>{comment.content}</p>
      <p>By: {comment.commenterusername}</p>
      <p><button onClick={() => handleReaction("comment", postId, postId, comment.id, "Like")}>Like</button>
         <button onClick={() => handleReaction("comment", postId, postId, comment.id, "Dislike")}>Dislike</button></p>
      {comment.commenterId === user.appUserID && 
        <button onClick={() => handleDeleteComment(user.appUserID, postId, comment.id)}>Delete Comment</button>}
    </div>
  );
};

export default Comment;