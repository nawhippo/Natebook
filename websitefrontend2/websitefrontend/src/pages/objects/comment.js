import React from 'react';
import { DeleteCommentButton } from '../buttonComponents/deleteCommentButton/deleteCommentButton';
import ReactionButtons from '../posts/reactPost/reactPost';

const Comment = ({ comment, user, postId, posterusername, handleReaction, fetchData }) => {
  return (
    <div key={comment.id} className='comment'>
      <p>{comment.content}</p>
      <p>By: {comment.commenterusername}</p>
      
      <ReactionButtons 
        user={user} 
        postId={postId} 
        posterusername={posterusername} 
        commentId={comment.id} 
        handleReaction={handleReaction} 
      />
      
      {user && (comment.commenterusername === user.username || posterusername === user.username) && 
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