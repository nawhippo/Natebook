import React from 'react';
import Comment from './comment'
import CommentForm from '../../buttonComponents/createCommentButton/createCommentButton';
import ReactionButtons from '../../buttonComponents/reactPostButtons/reactPostButtons';
import { DeletePostButton } from '../../buttonComponents/deletePostButton/deletePostButton';

const Post = ({ post, user, fetchData }) => {
  const [localLikesCount, setLocalLikesCount] = React.useState(post.likesCount);
  const [localDislikesCount, setLocalDislikesCount] = React.useState(post.dislikesCount);

  const updateLikesDislikes = (newLikes, newDislikes) => {
    setLocalLikesCount(newLikes);
    setLocalDislikesCount(newDislikes);
  };


  return (
    <div key={post.id}>
      <div>Title: {post.title}</div>
      <div>Description: {post.description}</div>
      <div>Posted By: {post.posterusername}</div>
      <div>{post.content}</div>

      {user.username === post.posterusername && (
        <DeletePostButton
          postId={post.id}
          fetchData={fetchData}
        />
      )}
      <p>Likes: {localLikesCount} Dislikes: {localDislikesCount}</p>
      <ReactionButtons 
      postId={post.id} 
      updateLikesDislikes={updateLikesDislikes}
      />

      <CommentForm
        userId={user.appUserID}
        posterusername={post.posterusername}
        postId={post.id}
      />

      {post.commentList && post.commentList.map((comment) => (
        <Comment
        key={comment.id}
        comment={comment}
        user={user}
        postId={post.id}
        posterId={post.posterid}
        fetchData={fetchData}
        updateLikesDislikes={updateLikesDislikes}
      />
      ))}
    </div>
  );
};

export default Post;