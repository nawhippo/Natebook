import React from 'react';
import CommentForm from '../buttonComponents/createCommentButton/createCommentButton';
import Comment from './comment'
import { DeletePostButton } from './DeletePostButton';
import ReactionButtons from './ReactionButtons';

const Post = ({ post, user, handleReaction, fetchData }) => {
  return (
    <div key={post.id}>
      <div>Title: {post.title}</div>
      <div>Description: {post.description}</div>
      <div>Posted By: {post.posterusername}</div>
      <div>{post.content}</div>

      <DeletePostButton
        userId={user.appUserID}
        posterusername={post.posterusername}
        postId={post.id}
        fetchData={fetchData}
      />
      
      <ReactionButtons user={user} postId={post.id} />

      <CommentForm
        userId={user.appUserID}
        posterusername={post.posterusername}
        postId={post.id}
      />

      {post.commentList && post.commentList.map((comment) => (
        <Comment
          comment={comment}
          user={user}
          postId={post.id}
          posterusername={post.posterusername}
          handleReaction={handleReaction}
          fetchData={fetchData}
        />
      ))}
    </div>
  );
};

export default Post;