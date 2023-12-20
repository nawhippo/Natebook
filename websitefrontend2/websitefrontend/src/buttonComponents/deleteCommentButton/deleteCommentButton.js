import React from 'react';
import {useUserContext} from "../../pages/usercontext/UserContext";

export const DeleteCommentButton = ({ posterusername, postId, commentId, targetUsername, fetchData }) => {
  const user = useUserContext();

  const buttonStyle = {
    backgroundColor: user && user.backgroundColor ? user.backgroundColor : 'orange'
  };
  const handleDeleteComment = async () => {
    console.log(typeof posterusername, typeof postId, typeof commentId);
    try {
      const response = await fetch(`http://localhost:3000/api/post/${posterusername}/${postId}/${commentId}/deleteComment`, { method: 'DELETE' });
      if (!response.ok) {
        throw new Error('Failed to delete.');
      }
      fetchData(targetUsername);
    } catch (error) {
      return error.message;
    }
  };

  return (
    <button style={buttonStyle} onClick={handleDeleteComment}>
      Delete Comment
    </button>
  );
};