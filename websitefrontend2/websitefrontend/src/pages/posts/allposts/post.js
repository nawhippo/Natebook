import React from 'react';
//to shorten posts page.
const Post = ({ post, user, handleReaction, handleDeletePost, handleCommentClick, currentPostId, CommentFormComponent }) => {

  
  return (
    <div key={post.id} className="post-card">
      <h2>{post.title}</h2>
      <p>{post.description}</p>
      <p><button onClick={() => handleReaction("post", post.id, post.posterid, null, "Like")}>Like</button>
         <button onClick={() => handleReaction("post", post.id, post.posterid, null, "Dislike")}>Dislike</button></p>
      <p>{post.email}</p>
      <p>{post.dateTime}</p>
      <p>By: {post.posterusername}</p>
      {post.posterid === user.appUserID && 
        <button onClick={() => handleDeletePost(user.appUserID, post.id)}>Delete Post</button>}
      
      <p>Comments:</p>
      <button onClick={() => handleCommentClick(post.id)}>Comment</button>
      {currentPostId === post.id && <CommentFormComponent postId={post.id} />}
    </div>
  );
};

export default Post;